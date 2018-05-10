package pl.sii.eu.micuenta.service.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.sii.eu.micuenta.model.Payment;

import java.io.IOException;

public class PaymentSerializer extends StdSerializer<Payment> {

    public PaymentSerializer() {
        this(null);
    }

    public PaymentSerializer(Class<Payment> t) {
        super(t);
    }

    @Override
    public void serialize(Payment payment, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", payment.getId());
        jsonGenerator.writeObjectField("repaymentDate", payment.getPaymentDate());
        jsonGenerator.writeStringField("clientId", payment.getClientId());
        jsonGenerator.writeNumberField("paymentAmount", payment.getPaymentAmount());
        jsonGenerator.writeObjectField("creditCard", payment.getCreditCard());
        jsonGenerator.writeEndObject();
    }
}