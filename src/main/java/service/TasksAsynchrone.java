package main.java.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.* ;
import java.util.stream.Collectors;

import main.java.model.Account;
import main.java.model.Bank;
import main.java.repository.AccountRepository;

public class TasksAsynchrone {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);


    public void startTask(AccountRepository accountRepository) {
        Runnable task = () -> {
            List<Account> listAccountCredit = accountRepository.getAccountsTypeCredit();
            for (Account account : listAccountCredit) {
                BigDecimal balance = account.getBalance();
                BigDecimal revenuMensuel = accountRepository.sallerAccount(account.getId());
                BigDecimal montantCredit = accountRepository.montoneCredit(account.getId());

                BigDecimal totalApresSalaire = balance.add(revenuMensuel);

                BigDecimal total = totalApresSalaire.subtract(montantCredit);

                Bank bank = accountRepository.getInfoBank("550e8400-e29b-41d4-a716-446655440000");
                BigDecimal capital = bank.getCapital();

                BigDecimal updatedCapital = capital.add(montantCredit);
                accountRepository.updateBankBalanceJust(updatedCapital, "550e8400-e29b-41d4-a716-446655440000");

                accountRepository.updateBalanceAccount(account.getId().toString(), total);

            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        scheduler.scheduleWithFixedDelay(task, 1, 5, TimeUnit.SECONDS);
    }
}