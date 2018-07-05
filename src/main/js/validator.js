var amf = require('amf-client-js')

var validator = {
    validate: function (apiKind, doc) {
        var profile = this.getProfile(apiKind)
        var messageStyle = this.getMessageStyle(apiKind)

        return amf.Core.validate(doc, profile, messageStyle)
    },

    getProfile: function (apiKind) {
        if (apiKind === "RAML10") return amf.ProfileNames.RAML
        if (apiKind === "RAML08") return amf.ProfileNames.RAML08
        if (apiKind === "OAS20") return  amf.ProfileNames.OAS
        return amf.ProfileNames.AMF
    },

    getMessageStyle: function (apiKind) {
        if (apiKind === "RAML10") return amf.MessageStyles.RAML
        if (apiKind === "RAML08") return amf.MessageStyles.RAML08
        if (apiKind === "OAS20") return  amf.MessageStyles.OAS
        return amf.MessageStyles.AMF
    }
}

exports.validator = validator;