package com.skysavvy.traveleasy.connection;

import com.amadeus.resources.FlightOrder.Traveler;
import com.amadeus.resources.FlightOrder.Document.DocumentType;
import com.amadeus.resources.FlightOrder.Phone.DeviceType;
import com.amadeus.resources.FlightOrder.Name;
import com.amadeus.resources.FlightOrder.Phone;
import com.amadeus.resources.FlightOrder.Contact;
import com.amadeus.resources.FlightOrder.Document;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DatabaseConnect {
    public static Traveler[] traveler(
    JsonArray travelerInfoArray) {
        Traveler[] travelers = new Traveler[travelerInfoArray.size()];
        for(int i = 0; i < travelerInfoArray.size(); i++) {
            JsonObject travelerInfo = travelerInfoArray.get(i).getAsJsonObject();
            String id = travelerInfo.get("id").getAsString();
            JsonObject name = travelerInfo.get("name").getAsJsonObject();
            String firstName = name.get("firstName").getAsString();
            String lastName = name.get("lastName").getAsString();
            String dateOfBirth = travelerInfo.get("dateOfBirth").getAsString();
            String gender = travelerInfo.get("gender").getAsString();
            Traveler traveler = new Traveler();
            Name n = new Name(firstName, lastName);
            traveler.setName(n);
            traveler.setDateOfBirth(dateOfBirth);
            traveler.setId(id);
            traveler.setGender(gender);
            Phone phone = new Phone();
            phone.setCountryCallingCode("1");
            phone.setNumber("1231231234");
            phone.setDeviceType(DeviceType.valueOf("MOBILE"));
            Contact contact = new Contact();
            Phone[] phones = {phone};
            contact.setPhones(phones);
            traveler.setContact(contact);
            Document document = new Document();
            document.setDocumentType(DocumentType.valueOf("PASSPORT"));
            document.setNumber("00000000");
            document.setExpiryDate("2025-04-14");
            document.setNationality("ES");
            document.setHolder(true);
            document.setIssuanceCountry("ES");
            Document[] documents = {document};
            traveler.setDocuments(documents);
            travelers[i] = traveler;
        }
        return travelers;
    }


}
