package com.example.transactions.service;

import com.example.transactions.client.AccountClient;
import com.example.transactions.client.NotificationClient;
import com.example.transactions.dto.request.NotificationCreateDto;
import com.example.transactions.dto.request.PutTransactionDto;
import com.example.transactions.dto.request.PostTransactionDto;
import com.example.transactions.dto.response.AccountDto;
import com.example.transactions.dto.response.ModifyTransactionCurrencyDto;
import com.example.transactions.dto.response.NotificationDto;
import com.example.transactions.dto.response.TransactionDto;
import com.example.transactions.entity.Transaction;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import com.example.transactions.mapper.TransactionMapper;
import com.example.transactions.repository.ITransactionRepository;
import com.example.transactions.service.ITransactionService;
import com.example.transactions.utils.TransactionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Implementarea serviciului pentru gestionarea tranzactiilor bancare.
 * Ofera functionalitati complete pentru ciclul de viata al unei tranzactii:
 * creare, actualizare, interogare, anulare, executare si verificare anti-frauda.
 *
 * @author Andrei Arustei
 */
@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private AccountClient accountClient;

    /**
     * Creeaza o noua tranzactie simpla fara detalii de cont sau notificari.
     * Primeste un DTO cu datele tranzactiei, il converteste in entitate si il salveaza in baza de date.
     *
     * @param transactionDto datele tranzactiei de creat
     * @return TransactionDto - tranzactia creata sub forma de DTO
     * @author Andrei Arustei
     */
    @Override
    public TransactionDto postTransaction(PostTransactionDto transactionDto) {
        Transaction transaction = TransactionMapper.ToEntity(transactionDto);
        transactionRepository.save(transaction);
        return TransactionMapper.ToDto(transaction);
    }

    /**
     * Creeaza o noua tranzactie cu detalii complete despre conturile implicate.
     * Apeleaza serviciul de conturi pentru a obtine informatii despre contul sursa si cel destinatie,
     * apoi creeaza tranzactia si returneaza un mesaj cu numele clientilor implicati.
     *
     * @param transactionDto datele tranzactiei de creat
     * @return String - mesaj de confirmare cu numele clientilor
     * @throws RuntimeException daca apelurile catre serviciul de conturi esueaza
     * @author Andrei Arustei
     */
    @Override
    public String postTransactionWithAccountDetails(PostTransactionDto transactionDto) {
        AccountDto fromAccountDto, toAccountDto;
        try {
            fromAccountDto = accountClient.fetchAccount(transactionDto.getFromAccountNumber()).getBody();
            toAccountDto = accountClient.fetchAccount(transactionDto.getToAccountNumber()).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch account for new transaction: " + e.getMessage());
        }

        Transaction transaction = TransactionMapper.ToEntity(transactionDto);
        transaction.setFromAccountNumber(fromAccountDto.getAccountNumber());
        transaction.setToAccountNumber(toAccountDto.getAccountNumber());
        transactionRepository.save(transaction);

        return "Transaction created successfully with account details: customer name "
                + fromAccountDto.getCustomerName() + " to " + toAccountDto.getCustomerName()
                + "transactionId: " + transaction.getTransactionId();
    }

    /**
     * Cauta o tranzactie existenta dupa ID si returneaza detalii despre conturile implicate.
     * Preia informatiile despre contul sursa si cel destinatie din serviciul de conturi.
     *
     * @param transactionId ID-ul unic al tranzactiei
     * @return String - mesaj cu detaliile clientilor implicati in tranzactie
     * @throws RuntimeException daca tranzactia nu este gasita sau apelurile catre serviciul de conturi esueaza
     * @author Andrei Arustei
     */
    @Override
    public String fetchTransactionWithAccountDetailsById(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findTransactionByTransactionId(transactionId);

        AccountDto fromAccountDto, toAccountDto;

        if(transaction.isPresent()) {
            try {
                fromAccountDto = accountClient.fetchAccount(transaction.get().getFromAccountNumber()).getBody();
                toAccountDto = accountClient.fetchAccount(transaction.get().getToAccountNumber()).getBody();

                return "Transaction getched successfully with account details: customer name "
                        + fromAccountDto.getCustomerName() + " to " + toAccountDto.getCustomerName()
                        + "transactionId: " + transaction.get().getTransactionId();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch account for new transaction: " + e.getMessage());
            }

        } else {
            throw new RuntimeException("Transaction with ID " + transactionId + " not found in method fetchTransactionById");
        }
    }

    /**
     * Creeaza o noua tranzactie si trimite automat o notificare catre destinatar.
     * Preia informatiile despre conturile implicate, genereaza si trimite o notificare SMS
     * catre clientul destinatar cu detaliile tranzactiei create.
     *
     * @param transactionDto datele tranzactiei de creat
     * @return String - mesaj de confirmare cu continutul notificarii trimise
     * @throws RuntimeException daca apelurile catre serviciul de conturi sau notificari esueaza
     * @author Andrei Arustei
     */
    @Override
    public String postTransactionWithNotification(PostTransactionDto transactionDto) {
        AccountDto fromAccountDto, toAccountDto;
        NotificationDto createdNotification;
        try {
            fromAccountDto = accountClient.fetchAccount(transactionDto.getFromAccountNumber()).getBody();
            toAccountDto = accountClient.fetchAccount(transactionDto.getToAccountNumber()).getBody();

            NotificationCreateDto notificationCreateDto = new NotificationCreateDto(
                    toAccountDto.getCustomerId(),
                    toAccountDto.getCustomerEmail(),
                    toAccountDto.getCustomerPhone(),
                    "SMS",
                    "TRANSACTION_CREATED",
                    "Transaction Created",
                    "Transaction from " + fromAccountDto.getCustomerName()
                            + " to " + toAccountDto.getCustomerName()
                            + " for amount " + transactionDto.getAmount() + " was created.",
                    "HIGH"
            );

             createdNotification = notificationClient.createNotification(notificationCreateDto).getBody();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create notification or fetch account for new transaction: " + e.getMessage());
        }

        Transaction transaction = TransactionMapper.ToEntity(transactionDto);
        transactionRepository.save(transaction);

        return "Transaction created successfully with new notification: " + createdNotification.getMessage()
                + " for transactionId: " + transaction.getTransactionId();
    }

    /**
     * Cauta si returneaza o tranzactie dupa ID-ul ei unic.
     * Preia tranzactia din baza de date si o converteste intr-un DTO pentru raspuns.
     *
     * @param transactionId ID-ul unic al tranzactiei
     * @return TransactionDto - tranzactia gasita sub forma de DTO
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita
     * @author Andrei Arustei
     */
    @Override
    public TransactionDto fetchTransactionById(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findTransactionByTransactionId(transactionId);

        if(transaction.isPresent()) {
            return TransactionMapper.ToDto(transaction.get());
        } else {
            throw new RuntimeException("Transaction with ID " + transactionId + " not found in method fetchTransactionById");
        }
    }

    /**
     * Actualizeaza o tranzactie existenta cu date noi.
     * Cauta tranzactia dupa ID, o actualizeaza cu datele din DTO si salveaza modificarile.
     *
     * @param transactionDto datele noi pentru actualizarea tranzactiei
     * @param transactionId ID-ul unic al tranzactiei de actualizat
     * @return boolean - true daca actualizarea a reusit
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita
     * @author Andrei Arustei
     */
    @Override
    public boolean putTransaction(PutTransactionDto transactionDto, String transactionId) {
        Optional<Transaction> transactionToPatch = transactionRepository.findTransactionByTransactionId(transactionId);

        if (transactionToPatch.isPresent()) {
            Transaction existing = transactionToPatch.get();
            Transaction updated = TransactionMapper.ToEntity(transactionDto, existing.getId(), transactionId);
            transactionRepository.save(updated);
            return true;
        }
        throw new RuntimeException("Transaction with ID " + transactionId + " not found in method patchTransaction");
    }

    /**
     * Anuleaza o tranzactie existenta schimband statusul ei in CANCELLED.
     * Cauta tranzactia dupa ID, modifica statusul si salveaza modificarea in baza de date.
     *
     * @param transactionId ID-ul unic al tranzactiei de anulat
     * @return boolean - true daca anularea a reusit
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita
     * @author Andrei Arustei
     */
    @Override
    public boolean cancelTransactionById(String transactionId) {
        Optional<Transaction> transactionToCancel = transactionRepository.findTransactionByTransactionId(transactionId);

        if(transactionToCancel.isPresent()) {
            transactionToCancel.get().setStatus(TransactionStatus.CANCELLED);
            transactionRepository.save(transactionToCancel.get());
            return true;
        }
        throw new RuntimeException("Transaction with ID " + transactionId + " not found in method cancelTransactionById");
    }

    /**
     * Executa o plata marcand tranzactia ca fiind finalizata (COMPLETED).
     * Cauta tranzactia dupa ID, schimba statusul in COMPLETED si salveaza modificarea.
     * Nota: Implementarea completa pentru transferul efectiv de bani va fi adaugata ulterior.
     *
     * @param transactionId ID-ul unic al tranzactiei de executat
     * @return boolean - true daca executarea a reusit
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita
     * @author Andrei Arustei
     */
    @Override
    public boolean executePaymentByTransactionId(String transactionId) {
        Optional<Transaction> transactionToExecutePayment = transactionRepository.findTransactionByTransactionId(transactionId);

        if(transactionToExecutePayment.isPresent()) {
            transactionToExecutePayment.get().setStatus(TransactionStatus.COMPLETED);
            // further implementation to move money from one account to another
            transactionRepository.save(transactionToExecutePayment.get());
            return true;
        }
        throw new RuntimeException("Transaction with ID " + transactionId + " not found in method executePaymentByTransactionId");
    }

    /**
     * Modifica tipul unei tranzactii existente.
     * Cauta tranzactia dupa ID, actualizeaza tipul tranzactiei cu noua valoare si salveaza modificarea.
     *
     * @param transactionId ID-ul unic al tranzactiei de modificat
     * @param newTransactionType noul tip de tranzactie (TRANSFER, WITHDRAWAL, DEPOSIT, PAYMENT)
     * @return boolean - true daca modificarea a reusit
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita
     * @author Andrei Arustei
     */
    @Override
    public boolean modifyTransactionType(String transactionId, TransactionType newTransactionType) {
        Optional<Transaction> transactionToExecutePayment = transactionRepository.findTransactionByTransactionId(transactionId);

        if(transactionToExecutePayment.isPresent()) {
            transactionToExecutePayment.get().setTransactionType(newTransactionType);
            transactionRepository.save(transactionToExecutePayment.get());
            return true;
        }
        throw new RuntimeException("Transaction with ID " + transactionId + " not found in method executePaymentByTransactionId");
    }

    /**
     * Modifica moneda unei tranzactii si recalculeaza automat suma in noua moneda.
     * Cauta tranzactia dupa ID, salveaza moneda curenta, seteaza noua moneda,
     * converteste suma in noua moneda folosind TransactionHelper si salveaza modificarile.
     *
     * @param transactionId ID-ul unic al tranzactiei de modificat
     * @param newCurrency noua moneda (RON, EUR, USD, GBP)
     * @return ModifyTransactionCurrencyDto - DTO cu ID-ul tranzactiei, noua moneda si suma convertita
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita
     * @author Andrei Arustei
     */
    @Override
    public ModifyTransactionCurrencyDto modifyTransactionCurrency(String transactionId, Currency newCurrency) {
        Optional<Transaction> transactionToModifyCurrency = transactionRepository.findTransactionByTransactionId(transactionId);

        if(transactionToModifyCurrency.isPresent()) {
            Currency previousCurrency = transactionToModifyCurrency.get().getCurrency();
            transactionToModifyCurrency.get().setCurrency(newCurrency);
            BigDecimal newAmount = (TransactionHelper.ConvertCurrency(
                    previousCurrency,
                    newCurrency,
                    transactionToModifyCurrency.get().getAmount()
            ));
            transactionToModifyCurrency.get().setAmount(newAmount);
            transactionRepository.save(transactionToModifyCurrency.get());

            return new ModifyTransactionCurrencyDto(
                    transactionId,
                    newCurrency,
                    newAmount
            );
        }
        throw new RuntimeException("Transaction with ID " + transactionId + " not found in method modifyTransactionCurrency");
    }

    /**
     * Calculeaza comisionul pentru o tranzactie in functie de tipul ei.
     * Aplica urmatoarele reguli:
     * - TRANSFER: 1% din suma (0.01)
     * - WITHDRAWAL: comision fix de 2.50
     * - DEPOSIT: fara comision (0)
     * - PAYMENT: 1.5% din suma (0.015)
     *
     * @param transactionId ID-ul unic al tranzactiei
     * @return BigDecimal - comisionul calculat pentru tranzactie
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita
     * @author Andrei Arustei
     */
    @Override
    public BigDecimal calculateTransactionAmount(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findTransactionByTransactionId(transactionId);

        if(transaction.isPresent()) {
            return switch (transaction.get().getTransactionType()) {
                case TRANSFER -> transaction.get().getAmount().multiply(new BigDecimal("0.01"));
                case WITHDRAWAL -> new BigDecimal("2.50");
                case DEPOSIT -> BigDecimal.ZERO;
                case PAYMENT -> transaction.get().getAmount().multiply(new BigDecimal("0.015"));
                default -> BigDecimal.ZERO;
            };
        }
        throw new RuntimeException("Transaction with ID " + transactionId + " not found in method generateTransactionAmount");
    }

    /**
     * Efectueaza o verificare anti-frauda asupra unei tranzactii finalizate.
     * Analizeaza tranzactia pentru a detecta potentiale activitati suspecte sau frauduloase.
     * Verificarea se aplica doar tranzactiilor cu status COMPLETED.
     *
     * @param transactionId ID-ul unic al tranzactiei de verificat
     * @return String - rezultatul verificarii anti-frauda
     * @throws RuntimeException daca tranzactia cu ID-ul specificat nu este gasita sau nu are status COMPLETED
     * @author Andrei Arustei
     */
    @Override
    public String antiFraudCheck(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findTransactionByTransactionId(transactionId);

        if(transaction.isPresent() && transaction.get().getStatus() == TransactionStatus.COMPLETED) {
            return TransactionHelper.checkAmountAntiFraud(transaction.get()).toString();
        }
        throw new RuntimeException("Transaction with ID " + transactionId + " not found in method antiFraudCheck");
    }
}
