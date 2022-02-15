# testing-fitnesse-myapifixtures

## Introduction
These are FitNesse (http://fitnesse.org) Fixtures für Rest Api Testing included in an Eclipse Java project.

Supported formats:
- Json

## FitNesse import fixtures
Use this to import the fixture to your test: (Don't forget to adjust the path)

```
classpath: /Users/jan/Documents/FitNesse/fitnesse-standalone.jar
classpath: /Users/jan/Documents/eclipse-workspace/MyApiFixtures/bin

variable defined: TEST_SYSTEM=fit 

|Import       |
|myApiFixtures|
```

## Simple usage
A simple request include three mandatory steps.

Init Request:
```
!|My Api Request Init                              |
|Method|Baseurl              |Path           |Init?|
|GET   |http://192.168.178.51|/mockme/api.php|ok   |
```

Send Request:
```
!|My Api Send                                      |
|Send Request?|Get Status Message?|Get Status Code?|
|ok           |OK                 |200             |
```

Validate Response:
```
!|My Api Response Body|
|Get Body?            |
|                     |
```

## Advanced usage - request
These Fixtures can be used to extend the test possibilities.

Add body to request
```
!|My Api Request Body |
|Body           |Init?|
|{"type":"test"}|ok   |
```

Add body to request from file
```
!|My Api Request Init                                                                                                                                    |
|Method|Baseurl              |Path           |Body File Path                                                                                       |Init?|
|POST  |http://192.168.178.51|/mockme/api.php|/Users/jan/Documents/FitNesse/FitNesseRoot/FitBackoffice/MyApiFixturesExampleSuite/SimpleRequest.json|ok   |
```

Add header to request
```
!|My Api Request Header                               |
|Name         |Value                            |Init?|
|Content-Type |application/x-www-form-urlencoded|ok   |
|Authorization|Bearer Token                     |ok   |
```

Add parameter to request
```
!|My Api Request Url Parameter|
|Name      |Value      |Init? |
|type      |test       |ok    |
|email     |fit@fit.de |ok    |
|password  |123abc!    |ok    |
```

## Advanced usage - response validation
These Fixtures can be used to validate the response.

In case of this is the response:
```
{"success":true,"basicdata":{"pointofsales":[{"id":"1","name":"Ebay","number":123},{"id":"2","name":"Ebay Kleinanzeigen","number":456},{"id":"3","name":"WooCommerce","number":789}],"plainarray":["bernd","nils","harry"]}}
```

This could be the validation
```
!|My Api Response Body Validator                                                                                     |
|Parser|Identifier                                    |Value?                                             |=Value?   |
|json  |root.success                                  |true                                               |res_symbol|
|json  |root.basicdata.pointofsales[1].name           |Ebay Kleinanzeigen                                 |res_symbol|
|json  |root.basicdata.pointofsales[id=3].name        |WooCommerce                                        |res_symbol|
|json  |root.basicdata.pointofsales["id"=3].name      |WooCommerce                                        |res_symbol|
|json  |root.basicdata.pointofsales[name="Ebay"].id   |1                                                  |res_symbol|
|json  |root.basicdata.pointofsales["name"="Ebay"].id |1                                                  |res_symbol|
|json  |root.basicdata.pointofsales[number=456].name  |Ebay Kleinanzeigen                                 |res_symbol|
|json  |root.basicdata.pointofsales["number"=456].name|Ebay Kleinanzeigen                                 |res_symbol|
|json  |root.basicdata.pointofsales[number="456"].name|Ebay Kleinanzeigen                                 |res_symbol|
|json  |root.basicdata.pointofsales[1]                |{"number":456,"name":"Ebay Kleinanzeigen","id":"2"}|res_symbol|
|json  |root.basicdata.pointofsales["number"=456]     |{"number":456,"name":"Ebay Kleinanzeigen","id":"2"}|res_symbol|
|json  |root.basicdata.plainarray[1]                  |nils                                               |res_symbol|
|json  |root.basicdata.pointofsales[count]            |3                                                  |res_symbol|
|json  |root.basicdata.plainarray[count]              |3                                                  |res_symbol|
```

## Examples
You can find more examples within the "MyApiFixturesExampleSuite"
