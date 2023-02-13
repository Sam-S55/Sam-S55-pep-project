package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService (AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts () {
        return accountDAO.getAllAccounts();
    }

    public Account addAccount (Account acct) {
        if (!acct.getUsername().isBlank() && acct.getPassword().length()>4 && findAccount(acct)==null) {
            return accountDAO.addAccount(acct);
        }
        return null;
    }

    public Account findAccount (Account acct) {
        List<Account> accts = getAllAccounts();
        for (Account a : accts) {
            if (a.getUsername().compareTo(acct.getUsername())==0 && a.getPassword().compareTo(acct.getPassword())==0) {
                return a;
            }
        }
        return null;
    }

}
