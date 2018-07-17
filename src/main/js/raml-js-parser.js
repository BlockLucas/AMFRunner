var jsRamlParser = require('raml-1-parser')

var parser = {
    parse: function (url) {
        console.log("Parsing/Validating file...");
        return jsRamlParser.loadRAML(url, url); 
    }
}

exports.parser = parser;