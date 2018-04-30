package pl.sii.eu.micuenta.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.sii.eu.micuenta.model.Debt;

import java.io.IOException;

public class DebtSerializer extends StdSerializer<Debt> {

    public DebtSerializer() {
        this(null);
    }

    public DebtSerializer(Class<Debt> t) {
        super(t);
    }

    @Override
    public void serialize(Debt debt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", debt.getId());
        jsonGenerator.writeNumberField("debtAmount", debt.getDebtAmount());
        jsonGenerator.writeStringField("repaymentDate", debt.getRepaymentDate().toString());
        jsonGenerator.writeObjectField("listOfPayments", debt.getSetOfPayments());
        jsonGenerator.writeEndObject();
    }
}
