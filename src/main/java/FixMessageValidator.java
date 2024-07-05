import quickfix.*;

import java.util.Iterator;

public class FixMessageValidator {
    private String fixMessage;

    public FixMessageValidator(String fixMessage) {
        this.fixMessage = fixMessage;
    }

    public boolean validate() {
        try {
            DataDictionary dataDictionary = new DataDictionary("FIX42.xml");
            Message parsedMessage = new Message(fixMessage, dataDictionary, true);
//            System.out.println(parsedMessage);

            System.out.println(fixMessage.split("\u0001").length);

//            FieldMap fp = parsedMessage;
//            System.out.println(fp);
//            Iterator iterator = fp.iterator();
//
//            System.out.println((iterator.hasNext()));
//            System.out.println(fp);
//
//            while(iterator.hasNext()) {
//                StringField field = (StringField)iterator.next();
//                int fieldTag = field.getTag();
//                String fieldValue = field.toString();
//
//                System.out.println("Field Tag: " + fieldTag + ", Field Value: " + fieldValue);
//            }

            System.out.println(
                    parsedMessage.getFieldOrder());


            for (String x : fixMessage.split("\u0001")) {
                System.out.println(parsedMessage);
                System.out.println("ENTER");
                try{
                    dataDictionary.validate(parsedMessage);
                    break;
                } catch (FieldException e) {
                    System.out.println("FIELD EXCEPTION");
                    // Validation Error
                    System.out.println(e.getField());
                    System.out.println(e.getMessage());
                    parsedMessage.getHeader().removeField(e.getField());
                    parsedMessage.removeField(e.getField());
                    System.out.println(parsedMessage);
                } catch (IncorrectTagValue | IncorrectDataFormat | FieldNotFound e){
                    // Missing Field
                    System.out.println("MISSING FIELD");
                    System.out.println(e.getMessage());
                }
            }
//            dataDictionary.validate(parsedMessage);
            return true;
        } catch (ConfigError e) {
            // Unable to init dataDictionary
            System.out.println(e.getMessage());
            return false;
        } catch (InvalidMessage e) {
            // Invalid Fix Message
            System.out.println(e.getMessage());
            return false;
        }
//        } catch (IncorrectTagValue | FieldNotFound | IncorrectDataFormat | FieldException e) {
//            // Validation Error
//            System.out.println(e.getMessage());
//            return false;
//        }
    }

    public static void main(String[] args) {
        String fixMessage = "8=FIX.4.2\u00019=120\u000135=QQ\u000149=SENDER\u000156=TARGET\u000134=1\u000152=20240704-10:30:00\u000110=32\u0001";
        FixMessageValidator validator = new FixMessageValidator(fixMessage);
        boolean isValid = validator.validate();

        if (isValid) {
            System.out.println("FIX message is valid");
        } else {
            System.out.println("FIX message is invalid");
        }
    }
}