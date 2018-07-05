var amf = require('amf-client-js')

var parser = {
    parse: function (apiKind, mode, content) {
        if (mode === 'text') {
            return this.parseString(apiKind, content)
        } else {
            return this.parseFile(apiKind, content)
        }
    },

    parseFile: function (apiKind, url) {
        console.log("Parsing file...")
        var parser = this.getParser(apiKind)
        return parser.parseFileAsync(url)
    },

    parseString: function (apiKind, api) {
        console.log("Parsing string...")
        var parser = this.getParser(apiKind)
        return parser.parseStringAsync(api)
    },

    getParser: function (apiKind) {
        if (apiKind === "RAML10") return amf.Core.parser("RAML 1.0", "application/yaml")
        if (apiKind === "RAML08") return amf.Core.parser("RAML 0.8", "application/yaml")
        if (apiKind === "OAS20") return  amf.Core.parser("OAS 2.0", "application/json")
        return amf.Core.parser("AMF Graph", "application/ld+json")
    }
}

exports.parser = parser;