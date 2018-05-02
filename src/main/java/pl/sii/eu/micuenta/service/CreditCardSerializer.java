package pl.sii.eu.micuenta.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.sii.eu.micuenta.model.CreditCard;

import java.io.IOException;

public class CreditCardSerializer extends StdSerializer<CreditCard> {

    public CreditCardSerializer() {
        this(null);
    }

    public CreditCardSerializer(Class<CreditCard> t) {
        super(t);
    }

    @Override
    public void serialize(CreditCard creditCard, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", creditCard.getId());
        jsonGenerator.writeStringField("cvv", creditCard.getCvv());
        jsonGenerator.writeStringField("ccNumber", creditCard.getCcNumber());
        jsonGenerator.writeStringField("firstName", creditCard.getFirstName());
        jsonGenerator.writeStringField("lastName", creditCard.getLastName());
        jsonGenerator.writeStringField("issuingNetwork", creditCard.getIssuingNetwork());
        jsonGenerator.writeStringField("expDate", creditCard.getExpDate().toString());
        jsonGenerator.writeEndObject();
    }
}