# CSV

A comma-separated values (CSV) file is a text file that uses a comma to separate an ordered sequence of values in a data record and a carriage return to separate the data records of a sequence.

A CSV can be represented as a list of lists in which the outer list captures the sequence of data records (representable as containers), while the inner list captures the sequence of primitive values within a record.

## Supported file extensions

.csv, .tsv, .tab

## Supported media types

text/csv, text/tab-separated-values

## Default Transformation


### Data:

```csv
email,name,surname
laura@example.com,Laura,Grey
craig@example.com,Craig,Johnson
mary@example.com,Mary,Jenkins
jamie@example.com,Jamie,Smith

```

### Query

```

CONSTRUCT
  {
    ?s ?p ?o .
  }
WHERE
  { SERVICE <x-sparql-anything:location=https://sparql-anything.cc/examples/simple.csv>
      { ?s  ?p  ?o }
  }

```

### Facade-X RDF:

```turtle
@prefix fx:   <http://sparql.xyz/facade-x/ns/> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .

[ a fx:root ;
  rdf:_1  [ rdf:_1  "laura@example.com" ;
            rdf:_2  "2070" ;
            rdf:_3  "Laura" ;
            rdf:_4  "Grey"
          ] ;
  rdf:_2  [ rdf:_1  "craig@example.com" ;
            rdf:_2  "4081" ;
            rdf:_3  "Craig" ;
            rdf:_4  "Johnson"
          ] ;
  rdf:_3  [ rdf:_1  "mary@example.com" ;
            rdf:_2  "9346" ;
            rdf:_3  "Mary" ;
            rdf:_4  "Jenkins"
          ] ;
  rdf:_4  [ rdf:_1  "jamie@example.com" ;
            rdf:_2  "5079" ;
            rdf:_3  "Jamie" ;
            rdf:_4  "Smith"
          ]
] .

```

## Options

### Summary

|Option name|Description|Valid Values|Default Value|
|-|-|-|-|
|csv.format|The format of the input CSV file.|Any predefined [CSVFormat](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html) of the Apache's commons CSV library|Default|
|csv.headers|It tells the CSV triplifier to use the headers of the CSV file for minting the properties of the generated triples.|true/false|false|
|csv.delimiter|The column delimiter, usually `,`,`;`,`\t`, ...|any single char|`,`|
|csv.quote-char|The quoting character|any single char|`"`|
|csv.null-string|It tells the CSV triplifier to not produce triples where the specificed string would be in the object position of the triple.|any string|not set|

### csv.format

#### Description

The format of the input CSV file.


#### Valid Values

Any predefined [CSVFormat](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html) of the Apache's commons CSV library.

#### Default Value

Default

#### Examples

##### Input

```

Sepal length	Sepal width	Petal length	Petal width	Species
5.1	3.5	1.4	0.2	I. setosa
4.9	3.0	1.4	0.2	I. setosa
4.7	3.2	1.3	0.2	I. setosa
4.6	3.1	1.5	0.2	I. setosa
5.0	3.6	1.4	0.2	I. setosa

```

Located at [https://sparql-anything.cc/examples/simple.tsv](https://sparql-anything.cc/examples/simple.tsv)

##### Use Case 1: Constructing a Facade-X RDF graph out of the TSV file available at https://sparql-anything.cc/examples/simple.tsv

###### Query

```

CONSTRUCT
  {
    ?s ?p ?o .
  }
WHERE
  { SERVICE <x-sparql-anything:location=https://sparql-anything.cc/examples/simple.tsv,csv.format=TDF>
      { ?s  ?p  ?o }
  }

```

###### Result

```

@prefix fx:  <http://sparql.xyz/facade-x/ns/> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix xyz: <http://sparql.xyz/facade-x/data/> .

[ rdf:type  fx:root ;
  rdf:_1    [ rdf:_1  "Sepal length" ;
              rdf:_2  "Sepal width" ;
              rdf:_3  "Petal length" ;
              rdf:_4  "Petal width" ;
              rdf:_5  "Species"
            ] ;
  rdf:_2    [ rdf:_1  "5.1" ;
              rdf:_2  "3.5" ;
              rdf:_3  "1.4" ;
              rdf:_4  "0.2" ;
              rdf:_5  "I. setosa"
            ] ;
  rdf:_3    [ rdf:_1  "4.9" ;
              rdf:_2  "3.0" ;
              rdf:_3  "1.4" ;
              rdf:_4  "0.2" ;
              rdf:_5  "I. setosa"
            ] ;
  rdf:_4    [ rdf:_1  "4.7" ;
              rdf:_2  "3.2" ;
              rdf:_3  "1.3" ;
              rdf:_4  "0.2" ;
              rdf:_5  "I. setosa"
            ] ;
  rdf:_5    [ rdf:_1  "4.6" ;
              rdf:_2  "3.1" ;
              rdf:_3  "1.5" ;
              rdf:_4  "0.2" ;
              rdf:_5  "I. setosa"
            ] ;
  rdf:_6    [ rdf:_1  "5.0" ;
              rdf:_2  "3.6" ;
              rdf:_3  "1.4" ;
              rdf:_4  "0.2" ;
              rdf:_5  "I. setosa"
            ]
] .

```


### csv.headers

|csv.headers||true/false|false|

#### Description

It tells the CSV triplifier to use the headers of the CSV file for minting the properties of the generated triples.

#### Valid Values

true/false

#### Default Value

false

#### Examples

##### Input

```

Sepal length	Sepal width	Petal length	Petal width	Species
5.1	3.5	1.4	0.2	I. setosa
4.9	3.0	1.4	0.2	I. setosa
4.7	3.2	1.3	0.2	I. setosa
4.6	3.1	1.5	0.2	I. setosa
5.0	3.6	1.4	0.2	I. setosa

```

Located at [https://sparql-anything.cc/examples/simple.tsv](https://sparql-anything.cc/examples/simple.tsv)


##### Use Case 1: Retrieving the sepal width of the species having sepal length greater that 4.9

###### Query

```
TODO
```

###### Result

```
TODO
```