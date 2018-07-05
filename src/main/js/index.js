var amf = require('amf-client-js')

var parser = require('./parser.js').parser;
var validator = require('./validator.js').validator;
var resolver = require('./resolver.js').resolver;

//Path is 'file:///' + full path to master api
var masterApiPath = 'file:///';
var apiKind = "RAML10"  //RAML10, RAML08 or OAS20

const PARSE = false;
const VALIDATE = false;
const RESOLUTION = true;
const VALIDATION_RESOLUTION = false;

amf.plugins.document.WebApi.register();
amf.plugins.document.Vocabularies.register();
amf.plugins.features.AMFValidation.register();
amf.Core.init().then ( function () {
    console.log("File: " + masterApiPath);

    if (PARSE) {
        console.log("Parsing...");
        parser.parse(apiKind, "file", masterApiPath).then(
            function(unit) {
                console.log("Parse OK");
                console.log(unit);
            }, 
            function(exception) {
                console.log("Parse Exception");
                console.log(exception);
            }
        )
    }

    if (VALIDATE) {
        console.log("Parsing...");
        parser.parse(apiKind, "file", masterApiPath).then(
            function(unit) {
                console.log("Parse OK");
                validator.validate(apiKind, unit).then(
                    function(report) {
                        if (report.conforms) {
                            console.log("Validation OK");
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
    }

    if (RESOLUTION) {
        console.log("Parsing...");
        parser.parse(apiKind, "file", masterApiPath).then(
            function(unit) {
                console.log("Parse OK");
                try {
                    var resolved = resolver.resolve(apiKind, unit);
                    console.log("Resolution OK");
                    console.log(resolved)
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
    }

    if (VALIDATION_RESOLUTION) {
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
    }
});
