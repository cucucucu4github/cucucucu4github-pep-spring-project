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
        String messageText = message.getMessageText();
        Integer postedBy = message.getPostedBy();
        
        if (messageText == null || messageText.length() < 1 || messageText.length() > 255)
            throw new MessageCreationFailedException("Message text must be between 1 and 255 characters.");

        if (!this.accountRepository.findById(postedBy).isPresent())
            throw new MessageCreationFailedException("Message postedBy does not matach an existing account id.");
        
        return this.messageRepository.save(message);
    }

    /**
     * Get all messages in database
     * @return List<Message> contains all messages. Can be empty.
     */
    public List<Message> getAllMessages(){
        return this.messageRepository.findAll();
    }

    /**
     * Get a specific message by message id.
     * @param id, message id
     * @return Message queried. Null if id not exist.
     */
    public Message getMessageById(int id) throws MessageIdNotExistsException{
        Optional<Message> optionMessage = this.messageRepository.findById(id);
        if(!optionMessage.isPresent()){
            throw new MessageIdNotExistsException("The message id does not exist.");
        }
        return optionMessage.get();
    }

    /**
     * Delete a specific message by message id.
     * @param id, message id.
     * @return number of records that been deleted.
     *  - 1 if deleted 1.
     *  - 0 if id not exist.
     */
    public int deleteMessageById(int id) {
        Optional<Message> optionalMessage = this.messageRepository.findById(id);
        
        if (!optionalMessage.isPresent()) {
            return 0;
        } else {
            this.messageRepository.deleteById(id);
            return 1;
        }
    }

    /**
     * Update a specific message text by given message id.
     * Message should exist.
     * message text should not over 255 characters.
     * @param id the message id.
     * @param messageText the new message text.
     * @return int, numbers of messages updated.
     */
    public int updateMessageText(int id, String messageText) throws MessageTextException, MessageIdNotExistsException {
        
        if(messageText == null || messageText.length() < 1 || messageText.length() > 255 || messageText.equals("")) {
            throw new MessageTextException("Message text not exist or empty or over 255 characters.");
        }

        Optional<Message> optionalMessage = this.messageRepository.findById(id);
        
        if (!optionalMessage.isPresent()) {
            throw new MessageIdNotExistsException("The message id does not exist.");
        } else {
            Message message = optionalMessage.get();
            message.setMessageText(messageText);
            this.messageRepository.save(message);
            return 1;
        }
    }

    /**
     * get all messages belong to a soecific account id.
     * @param accoundId
     * @return List<Message> contains all messages by a spcecific account id. Could be empty.
     */
    public List<Message> getAllMessagesByAccountId(int accoundId){
        return this.messageRepository.findByPostedBy(accoundId);
    }
}
