package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class serves the purpose of sending out confirmation e-mails after an order was made in the shop.
 * It consists of two methods. While the sendEmail method first sets up the appropriate environment and then
 * sends out the corresponding e-mail, the convertOrder concatenates the items from the order to a string so
 * the sendEmail can then use it.
 *
 * @author  Peter Bernath
 * @version 1.0
 * @since   2018-01-17
 */
public class Email {

    private static final Logger logger = LoggerFactory.getLogger(Email.class);


    /**
     * This methods sends out confirmation e-mails after an order.
     * <p>
     * It has 2 private fields, where the developer can set up the username and password for the email
     * address, from which the confirmation e-mails will be sent out. Then it sets up the properties
     * and creates a session with them. Then it sets up recipients, subject and finally concatenates a short
     * message with all the order information and sends it out to the customer. If the sending was unsuccessful,
     * it throws a RuntimeException.
     * @param order the parameter is used to get information about the order which is then composed to the e-mail
     */
    public static void sendEmail(Order order, User user) {

        final String username = "codecoolshop.angrynerds@gmail.com";
        final String password = "Honolulu27";
        Map userData = user.getUserData();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        logger.trace("Properties set up");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        logger.trace("Session created");
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("peter.bernath.27@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse((String) userData.get("E-mail")));
            message.setSubject("Order No. " + order.getId() + " Confirmation");
            message.setText("Dear " + userData.get("Name") + ","
                    + "\n\n Thank You for your order! Your order details can be found below in this e-mail."
                    + "\n\n Order number: " + order.getId()
                    + "\n Name: " + userData.get("Name")
                    + "\n E-mail: " + userData.get("E-mail")
                    + "\n Phone Number: " + userData.get("Phone Number")
                    + "\n Billing Address: " + userData.get("Billing Address")
                    + "\n Billing City: " + userData.get("Billing City")
                    + "\n Billing Zipcode: " + userData.get("Billing Zipcode")
                    + "\n Billing Country: " + userData.get("Billing Country")
                    + "\n Shipping Address: " + userData.get("Shipping Address")
                    + "\n Shipping City: " + userData.get("Shipping City")
                    + "\n Shipping Zipcode: " + userData.get("Shipping Zipcode")
                    + "\n Shipping Country: " + userData.get("Shipping Country")
                    + "\n\n Ordered items: \n"
                    + convertOrder(order.getCart())
                    + "\n\n We wish You merry christmas and hope You will shop with us next time!"
                    + "\n Best regards,"
                    + "\n\n Your CodeCool Shop");
            logger.trace("E-mail created");

            Transport.send(message);

            logger.debug("E-mail sent");

        } catch (MessagingException e) {
            logger.warn("An error occured during the process of sending out confirmation e-mail");
            throw new RuntimeException(e);
        }

    }

    /**
     * This simple method concatenates the items from an order into a string so the sendEmail method can then
     * easily use it during message composing.
     * @param map this map is basically a product list which contains all the ordered products and the count of them
     * @return the method returns the order in string format
     */
    private static String convertOrder(Map<Product, Integer> map) {
        String orderString = "";
        int total = 0;
        for (Product name: map.keySet()){

            String key = name.getName();
            String value = map.get(name).toString();
            String price = name.getPrice();
            orderString += "\n " + value + "x   " + key + "       " + price;
            total += Integer.parseInt(name.getPrice().substring(0, name.getPrice().length() - 6)) * Integer.parseInt(value);
        }
        orderString += "\n --------------------------------------------------";
        orderString += "\n Total amount paid:        " + String.valueOf(total) + " USD";
        logger.trace("Concatenated order into string format to be able to insert it into e-mail");
        return orderString;
    }

}
