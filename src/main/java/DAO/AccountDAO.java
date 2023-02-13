package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public List<Account> getAllAccounts () {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account acct = new Account(rs.getInt("account_id"), 
                rs.getString("username"),
                rs.getString("password"));
                accts.add(acct);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accts;
    }

    public Account addAccount (Account acct) {
        Connection connection = ConnectionUtil.getConnection();
        try {   
            String sql = "INSERT INTO account (username,password) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, acct.getUsername());
            ps.setString(2, acct.getPassword());

            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_acct_id = pkeyResultSet.getInt(1);
                Account a = new Account(generated_acct_id, acct.getUsername(), acct.getPassword());
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
