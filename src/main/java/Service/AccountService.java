package Service;

import Model.Account;
import DAO.AccountDAO;
import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService () {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account getUserByUsername(String user) {
        return accountDAO.getUserByUsername(user);
    }

    public Account userLogin(Account user) {
        return accountDAO.userLogin(user);
    }
}
