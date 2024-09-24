package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.exception.*;

@Service
public class MessageService {
    private AccountRepository accountRepository;
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(AccountRepository accountRepository, MessageRepository messageRepository){
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * Create a new message.
     * @param message, should contain:
     *  - message text, length between 1~255
     *  - postedBy, matches existing account id
     *  - timePostedEpoch, not empty
     * @return Message just created with message id if all condition satisfied.
     * @throws MessageCreationFailedException
     */
    public Message createMessage(Message message) throws MessageCreationFailedException{
        return null;
    }

    /**
     * Get all messages in database
     * @return List<Message> contains all messages. Can be empty.
     */
    public List<Message> getAllMessages(){
        return null;
    }

    /**
     * Get a specific message by message id.
     * @param id, message id
     * @return Message queried. Null if id not exist.
     */
    public Message getMessageById(int id){
        return null;
    }

    /**
     * Delete a specific message by message id.
     * @param id, message id.
     * @return number of records that been deleted.
     *  - 1 if deleted 1.
     *  - 0 if id not exist.
     */
    public int deleteMessageById(int id){
        return 0;
    }

    /**
     * Update a specific message text by given message id.
     * Message should exist.
     * message text should not over 255 characters.
     * @param id the message id.
     * @param messageText the new message text.
     * @return int, numbers of messages updated.
     */
    public int updateMessageText(int id, String messageText){
        return 1;
    }

    /**
     * get all messages belong to a soecific account id.
     * @param accoundId
     * @return List<Message> contains all messages by a spcecific account id. Could be empty.
     */
    public List<Message> getAllMessagesByAccountId(int accoundId){
        return null;
    }
}
