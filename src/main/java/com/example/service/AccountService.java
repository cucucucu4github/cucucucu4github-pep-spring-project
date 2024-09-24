package com.example.service;

import org.springframework.stereotype.Service;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.exception.*;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /**
     * Service that register a new account, store accoutn info to database.
     * @param inputAccount, should contain:
     *  - String username: not empty, not exist.
     *  - String password: length >= 4.
     * @return 
     *  - if success, return the Account just stored onto database with account id. 
     *  - if username already exists, throws UsernameAlreadyExistsException. 
     *  - Otherwise, throws RegisterFailedException. 
     */
    public Account registerService(Account inputAccount) throws RegisterFailedException, UsernameAlreadyExistsException{

        if(inputAccount.getUsername() == null || inputAccount.getUsername().length() < 1)
            throw new RegisterFailedException("The username not exiists or is empty.");
        if(inputAccount.getPassword() == null || inputAccount.getPassword().length() < 4)
            throw new RegisterFailedException("The password not exists or too short, should contain at least 4 characters.");

        Optional<Account> optionAccount = this.accountRepository.findByUsername(inputAccount.getUsername());
        if(optionAccount.isPresent()){
            throw new UsernameAlreadyExistsException("The username you want to register already exists.");
        } else {
            return this.accountRepository.save(inputAccount);
        }
    }

    /**
     * Service that login an account.
     * @param inputAccount, should contain:
     *  - String username: matches an exist account record.
     *  - String password: matches the existing account with given account username.
     * @return
     *  - If both matches, return account with account id.
     *  - Otherwise, throws LoginFailedException.
     */
    public Account loginService(Account inputAccount) throws LoginFailedException {
        Account account = accountRepository.findByUsername(inputAccount.getUsername())
            .orElseThrow(() -> new LoginFailedException("The username you input does not exist."));
        
        if (!account.getPassword().equals(inputAccount.getPassword())) {
            throw new LoginFailedException("The password does not match the account.");
        }
    
        return account;
    }
}
