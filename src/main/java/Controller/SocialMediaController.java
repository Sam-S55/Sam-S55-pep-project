package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::userRegistrationHandler);
        app.post("/login", this::userLoginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessagesByIDHandler);
        app.delete("/messages/{message_id}", this::deleteByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void userRegistrationHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account a = mapper.readValue(ctx.body(), Account.class);
        Account added = accountService.addAccount(a);
        if(added!=null){
            ctx.json(mapper.writeValueAsString(added));
        }else{
            ctx.status(400);
        }
    }

    private void userLoginHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account a = mapper.readValue(ctx.body(), Account.class);
        Account ver = accountService.findAccount(a);
        if (ver!=null) {
            ctx.json(mapper.writeValueAsString(ver));
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.createMessage(m);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getMessagesHandler (Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessagesByIDHandler (Context ctx) {
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.getMessageByID(msg_id);
        if (msg != null) {
            ctx.json(msg);
        } else {
            ctx.status(200);
        }
    }

    private void deleteByIDHandler (Context ctx) throws JsonMappingException, JsonProcessingException {
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.getMessageByID(msg_id);
        if (msg != null) {
            ctx.json(messageService.deleteMessageByID(msg_id));
        } else {
            ctx.status(200);
        }
    }

    private void updateMessageHandler (Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        int m_id = Integer.parseInt(ctx.pathParam("message_id"));
        String update = msg.getMessage_text();
        Message updatedMessage = messageService.updateMessage(m_id,update);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(updatedMessage);
        }
    }

    private void getMessagesByAccountHandler (Context ctx) {
        ctx.json(messageService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("{account_id}"))));
    }

}