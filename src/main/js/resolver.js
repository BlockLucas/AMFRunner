var amf = require('amf-client-js')

var resolver = {
    resolve: function (apiKind, unit) {
        var resolver = this.getResolver(apiKind)
        return resolver.resolve(unit)
    },

    getResolver: function (apiKind) {
        if (apiKind === "RAML10") return amf.Core.resolver("RAML 1.0")
        if (apiKind === "RAML08") return amf.Core.resolver("RAML 0.8")
        if (apiKind === "OAS20") return  amf.Core.resolver("OAS 2.0")
        return amf.Core.resolver("AMF Graph")
    }
}

exports.resolver = resolver;