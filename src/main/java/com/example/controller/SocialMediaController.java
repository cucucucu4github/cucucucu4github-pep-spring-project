package com.example.controller;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.RegisterFailedException;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.exception.*;

import java.util.List;
import java.util.ArrayList;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
 public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Handler to create new account.
     * Success when username not empty and not exist, password length >= 4.
     * If success, return account JSON with account id, with status code 200
     * If account already exist, return 409.
     * Otherwise, return 400.
     * @param inputAccount repuest body, should contain username, password.
     * @return ResponseEntity<Account>
     */
    @PostMapping(value="/register")
    public ResponseEntity<Account> registerHandler(@RequestBody Account inputAccount){
        try {
            Account account = this.accountService.registerService(inputAccount);
            return ResponseEntity.status(200)
                                 .body(account);
        } catch (RegisterFailedException e) {
            System.err.println("Register failed: " + e.getMessage());
            return ResponseEntity.status(400)
                                 .body(null);
        } catch (UsernameAlreadyExistsException e){
            System.err.println("Register failed: " + e.getMessage());
            return ResponseEntity.status(409)
                                 .body(null);
        }
    }
    /**
     * Handler to check user login.
     * Success when username exist, and password matches.
     * If success, return account JSON with account id, with status code 200.
     * Otherwise, return 401.
     * @param inputAccount repuest body, should contain username, password.
     * @return ResponseEntity<Account>
     */
    @PostMapping(value="/login")
    public ResponseEntity<Account> loginHandler(@RequestBody Account inputAccount){
        try {
            Account account = this.accountService.loginService(inputAccount);
            return ResponseEntity.status(200)
                                 .body(account);
        } catch (LoginFailedException e) {
            System.err.println("Login failed: " + e.getMessage());
            return ResponseEntity.status(401)
                                 .body(null);
        }
    }

    /**
     * Handler to create new Message
     * Success if messageText length > 0 and <= 255, and postedBy matches an exist account id.
     * If success, return Message JSON with message id, with status 200.
     * Otherwise status 400.
     * @param message Request body, should contain message text and postedBy
     * @return ResponseEntity<Message>
     */
    @PostMapping(value="/messages")
    public ResponseEntity<Message> createMessageHandler(@RequestBody Message inputMessage){
        try{
            Message message = this.messageService.createMessage(inputMessage);
            return ResponseEntity.status(200)
                                 .body(message);
        } catch(MessageCreationFailedException e) {
            System.err.println("Message creation failed: " + e.getMessage());
            return ResponseEntity.status(400)
                                 .body(null);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }

    /**
     * Handler to query all messages.
     * Return all messages, with status 200.
     * @return ResponseEntity<List<Message>>
     */
    @GetMapping(value="/messages")
    public ResponseEntity<List<Message>> queryAllMessageHandler(){
        return ResponseEntity.status(200).body(this.messageService.getAllMessages());
    }

    /**
     * Handler to query a message by message id.
     * If Message exist, return JSON message with status 200.
     * Otherwise, return empty with status 400.
     * @param messageId the ID of the message to be retrieved
     * @return ResponseEntity<Message>
     */
    @GetMapping(value="/messages/{messageId}")
    public ResponseEntity<Message> queryMessageByIdHandler(@PathVariable String messageId){
        try{
            Message message = this.messageService.getMessageById(Integer.valueOf(messageId));
            return ResponseEntity.status(200)
                                 .body(message);
        } catch(MessageIdNotExistsException e){
            System.err.println("Message does not exist: " + e.getMessage());
            return ResponseEntity.status(200)
                                 .body(null);
        }
    }

    /**
     * Delete a message by message id.
     * the request url should contain the message id.
     * If message exist, delete the message and return the line number been infeccted (should be 1)
     * Otherwise, return with number 0.
     * @param messageId the ID of the message to be deleted
     * @return ResponseEntity<Integer>
     */
    @DeleteMapping(value="/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByIdHandler(@PathVariable String messageId){
        int recordsDeleted = this.messageService.deleteMessageById(Integer.valueOf(messageId));
        return recordsDeleted == 1? 
            ResponseEntity.status(200).body(1) : ResponseEntity.status(200).body(null);
                             
    }

    /**
     * Update an exist message.
     * The request url should contain message id.
     * The request body should contain message text.
     * If message id exist and message text length between 1 ~ 255, success.
     * If success, update the database, return with number 1 (the records number been updated) with status 200.
     * Otherwise, return 0 with status 400.
     * @param messageId the ID of the message to be updated
     * @param inputMessage request body should only contain message text
     * @return
     */
    @PatchMapping(value="/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByIdHandler(@PathVariable String messageId, @RequestBody Message inputMessage){

        String messageText = inputMessage.getMessageText();

        try{
            int recordsUpdated = this.messageService.updateMessageText(Integer.valueOf(messageId), messageText);
            return ResponseEntity.status(200)
                                 .body(1);
        } catch(MessageIdNotExistsException e){
            System.err.println("Message update failed: " + e.getMessage());
            return ResponseEntity.status(400)
                                 .body(null);
        } catch(MessageTextException e) {
            System.err.println("Message update failed: " + e.getMessage());
            return ResponseEntity.status(400)
                                 .body(null);
        }
    }

    /**
     * Query all messages by accoutn id.
     * Always return with all messages queried with given id, with status 200.
     * @param accountId the ID of the account whose messages are to be retrieved
     * @return ResponseEntity<List<Message>> 
     */
    @GetMapping(value="/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> queryAllMessagesByAccountIdHandler(@PathVariable String accountId){
        return ResponseEntity.status(200).body(this.messageService.getAllMessagesByAccountId(Integer.valueOf(accountId)));
    }

}
