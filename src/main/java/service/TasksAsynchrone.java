package main.java.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.* ;
import java.util.stream.Collectors;

import main.java.model.Account;
import main.java.repository.AccountRepository;

public class TasksAsynchrone {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2) ;


    public void StartTask(AccountRepository accountRepository)
    {
        Runnable task = () -> {
            System.out.println("helllo i'm abde louafi " + java.time.LocalTime.now());
            List<Account> listAccountCredit = accountRepository.getAccountsTypeCredit() ;
            listAccountCredit.forEach(acc -> {
                System.out.println("ID: " + acc.getId() +
                        ", Type: " + acc.getType() +
                        ", Balance: " + acc.getBalance());
            });

            for(Account account : listAccountCredit)
            {
                BigDecimal balance = account.getBalance() ;
                BigDecimal revenu_mensuel =  accountRepository.sallerAccount(account.getId()) ;
                BigDecimal total = balance.add(revenu_mensuel) ;
                System.out.println("id = " + account);
                System.out.println("account balance =======" + balance);
                System.out.println("account revenu_mensuel =======" + revenu_mensuel);
                System.out.println("account total =======" + total);

                accountRepository.updateBalanceAccount(account.getId().toString(),total);
                BigDecimal newbalance = account.getBalance() ;

                System.out.println("account updated =======" + newbalance);
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
