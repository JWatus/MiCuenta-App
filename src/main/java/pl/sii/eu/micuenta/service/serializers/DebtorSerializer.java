package pl.sii.eu.micuenta.service.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.sii.eu.micuenta.model.Debtor;

import java.io.IOException;

public class DebtorSerializer extends StdSerializer<Debtor> {

    public DebtorSerializer() {
        this(null);
    }

    public DebtorSerializer(Class<Debtor> t) {
        super(t);
    }

    @Override
    public void serialize(Debtor debtor, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", debtor.getId());
        jsonGenerator.writeStringField("firstName", debtor.getFirstName());
        jsonGenerator.writeStringField("lastName", debtor.getLastName());
        jsonGenerator.writeStringField("ssn", debtor.getSsn());
        jsonGenerator.writeObjectField("listOfDebts", debtor.getSetOfDebts());
        jsonGenerator.writeEndObject();
    }
}
