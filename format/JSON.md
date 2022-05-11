# JSON

## Default transformation

Data:

```json
{
    "stringArg": "stringValue",
    "intArg": 1,
    "booleanArg": true,
    "nullArg": null,
    "arr": [ 0, 1 ]
}
```

Facade-X RDF:

```turtle
@prefix xyz:    <http://sparql.xyz/facade-x/data/> .
@prefix fx:   <http://sparql.xyz/facade-x/ns/> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
[ a fx:root ;
    xyz:arr [
        rdf:_1  "0"^^xsd:int ;
        rdf:_2  "1"^^xsd:int ] ;
    xyz:booleanArg  true ;
    xyz:intArg "1"^^xsd:int ;
    xyz:stringArg "stringValue"
] .
```

## Options

### json.path

#### Description

One or more JsonPath expressions as filters. E.g. `json.path=value` or `json.path.1`, `json.path.2`, `...` to add multiple expressions.

#### Valid Values

Any valid JsonPath (see [JsonSurfer implementation](https://github.com/jsurfer/JsonSurfer))

#### Default Value

Not set

#### Example

Input:

```json
[
  {
    "name":"Friends",
    "genres":[
      "Comedy",
      "Romance"
    ],
    "language":"English",
    "status":"Ended",
    "premiered":"1994-09-22",
    "summary":"Follows the personal and professional lives of six twenty to thirty-something-year-old friends living in Manhattan.",
    "stars":[
      "Jennifer Aniston",
      "Courteney Cox",
      "Lisa Kudrow",
      "Matt LeBlanc",
      "Matthew Perry",
      "David Schwimmer"
    ]
  },
  {
    "name":"Cougar Town",
    "genres":[
      "Comedy",
      "Romance"
    ],
    "language":"English",
    "status":"Ended",
    "premiered":"2009-09-23",
    "summary":"Jules is a recently divorced mother who has to face the unkind realities of dating in a world obsessed with beauty and youth. As she becomes older, she starts discovering herself.",
    "stars":[
      "Courteney Cox",
      "David Arquette",
      "Bill Lawrence",
      "Linda Videtti Figueiredo",
      "Blake McCormick"
    ]
  }
]
```

##### Use Case 1: Constructing a Facade-X RDF Graph only only containers that match the Json Path ``$[?(@.name=="Friends")]``.

###### Query

```

PREFIX  xyz:  <http://sparql.xyz/facade-x/data/>
PREFIX  fx:   <http://sparql.xyz/facade-x/ns/>
PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

CONSTRUCT
  {
    ?s ?p ?o .
  }
WHERE
  { SERVICE <x-sparql-anything:location=json.json>
      { fx:properties
                  fx:json.path  "$[?(@.name==\"Friends\")]" .
        ?s        ?p            ?o
      }
  }

```

###### Result

```turtle

@prefix fx:  <http://sparql.xyz/facade-x/ns/> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix xyz: <http://sparql.xyz/facade-x/data/> .

[ rdf:type  fx:root ;
  rdf:_1    [ xyz:genres     [ rdf:_1  "Comedy" ;
                               rdf:_2  "Romance"
                             ] ;
              xyz:language   "English" ;
              xyz:name       "Friends" ;
              xyz:premiered  "1994-09-22" ;
              xyz:stars      [ rdf:_1  "Jennifer Aniston" ;
                               rdf:_2  "Courteney Cox" ;
                               rdf:_3  "Lisa Kudrow" ;
                               rdf:_4  "Matt LeBlanc" ;
                               rdf:_5  "Matthew Perry" ;
                               rdf:_6  "David Schwimmer"
                             ] ;
              xyz:status     "Ended" ;
              xyz:summary    "Follows the personal and professional lives of six twenty to thirty-something-year-old friends living in Manhattan."
            ]
] .


```

##### Use Case 2: Retrieving the language of the TV series named "Friends".

###### Query

```
PREFIX  xyz:  <http://sparql.xyz/facade-x/data/>
PREFIX  fx:   <http://sparql.xyz/facade-x/ns/>
PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

SELECT  ?language
WHERE
  { SERVICE <x-sparql-anything:location=json2.json>
      { fx:properties
                  fx:json.path  "$[?(@.name==\"Friends\")]" .
        _:b0      xyz:language  ?language
      }
  }
```

###### Result

```
-------------
| language  |
-------------
| "English" |
-------------
```