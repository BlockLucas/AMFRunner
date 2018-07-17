var amf = require('amf-client-js')

var parser = require('./parser.js').parser;
var validator = require('./validator.js').validator;
var resolver = require('./resolver.js').resolver;
var jsParser = require('./raml-js-parser.js').parser;

// Path is 'file:///' + full path to master api
// Example in Mac: 'file:///Users/username/testing/lib-traits.raml' / Example in Windows: 'file:///C:/username/testing/lib-traits.raml'
var masterApiPath = '';
var apiKind = "RAML10"  //RAML10, RAML08 or OAS20

const PARSE = false;
const VALIDATE = false;
const RESOLUTION = false;
const VALIDATION_RESOLUTION = false;
const JS_PARSER = false;

amf.AMF.init().then ( function () {
    console.log("File: " + masterApiPath);

    if (PARSE) {
        try {
            console.log("Parsing...");
            parser.parse(apiKind, "file", masterApiPath).then(
                function(unit) {
                    console.log("Parse OK");
                    //console.log(unit);
                },
                function(exception) {
                    console.log("Parse Error");
                    console.log(exception);
                }
            )
        } catch(ex) {
            console.log("Parse Exception");
            console.log(exception);
        }
    }

    if (VALIDATE) {
        console.log("Parsing...");
        try {
            parser.parse(apiKind, "file", masterApiPath).then(
                function(unit) {
                    console.log("Parse OK");
                    validator.validate(apiKind, unit).then(
                        function(report) {
                            if (report.conforms) {
                                console.log("Validation OK");
                                console.log(report.toString());
                            } else {
                                console.log("Validation Error");
                                console.log(report.toString());
                            }
                        },
                        function(exception) {
                            console.log("Validation Exception");
                            console.log(exception);
                        }
                    )
                },
                function(exception) {
                    console.log("Parse Exception");
                    console.log(exception);
                }
            )
        } catch(ex) {
            console.log("Parse Exception");
            console.log(exception);
        }
    }

    if (RESOLUTION) {
        try {
            console.log("Parsing...");
            parser.parse(apiKind, "file", masterApiPath).then(
                function(unit) {
                    console.log("Parse OK");
                    try {
                        var resolved = resolver.resolve(apiKind, unit);
                        console.log("Resolution OK");
                        //console.log(resolved)
                    } catch(err) {
                        console.log("Resolution Error");
                        console.log(err);
                    }
                },
                function(exception) {
                    console.log("Parse Exception");
                    console.log(exception);
                }
            )
        } catch(ex) {
            console.log("Parse Exception");
            console.log(exception);
        }
    }

    if (VALIDATION_RESOLUTION) {
        try {
            console.log("Parsing...");
            parser.parse(apiKind, "file", masterApiPath).then(
                function(unit) {
                    console.log("Parse OK");
                    try {
                        var resolved = resolver.resolve(apiKind, unit);
                        console.log("Resolution OK");
                        validator.validate(apiKind, resolved).then(
                            function(report) {
                                if (report.conforms) {
                                    console.log("Validation-Resolution OK");
                                } else {
                                    console.log("Validation-Resolution Error");
                                    console.log(report.toString());
                                }
                            },
                            function(exception) {
                                console.log("Validation-Resolution Exception");
                                console.log(exception);
                            }
                        )
                    } catch(err) {
                        console.log("Resolution Error");
                        console.log(err);
                    }
                },
                function(exception) {
                    console.log("Parse Exception");
                    console.log(exception);
                }
            )
        } catch(ex) {
            console.log("Parse Exception");
            console.log(exception);
        }
    }

    if (JS_PARSER) {
        try {
            var path = masterApiPath.substring(7) // Because the path for js-raml-parser is without 'file://'
            jsParser.parse(path).then(
                function(api) {
                    if (api.errors().length === 0) {
                        var expanded = api["expand"]().toJSON({rootNodeDetails: true})
                        console.log("JS Raml Parse-Validation OK")
                        //console.log(expanded)
                    } else {
                        console.log("JS Raml Parse-Validation ERROR")
                        api.errors().forEach(function(x){
                            console.log(JSON.stringify({
                                code: x.code,
                                message: x.message,
                                path: x.path,
                                start: x.start,
                                end: x.end,
                                isWarning: x.isWarning
                            },null,2));
                        });
                    }
                },
                function(exception) {
                    console.log("JSParse Exception");
                    console.log(exception);
                }
            )
        } catch(ex) {
            console.log("JSParse Exception");
            console.log(exception);
        }
    }
});
