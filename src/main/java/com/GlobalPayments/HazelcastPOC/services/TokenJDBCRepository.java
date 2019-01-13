package com.GlobalPayments.HazelcastPOC.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.GlobalPayments.HazelcastPOC.model.Token;
@Repository
public class TokenJDBCRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    class TokenRowMapper implements RowMapper < Token > {
        @Override
        public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
            Token token = new Token();
            token.setPanToken(rs.getString("pan_Token"));
            token.setMerchantToken(rs.getString("merchant_Token"));
            token.setGlobalToken(rs.getString("global_Token"));
            return token;
        }
    }
    public List < Token > findAll() {
        return jdbcTemplate.query("select * from token", new TokenRowMapper());
    }
    public Token findByPANToken(String panToken) {
        return jdbcTemplate.queryForObject("select * from token where pan_Token=?", new Object[] {
                panToken
            },
            new BeanPropertyRowMapper < Token > (Token.class));
    }
    
    public List<Token> findByMerchantToken(String merchantToken) {
    	
        return jdbcTemplate.query("select * from token where merchant_Token=?", new Object[] {
                merchantToken
            },
            new BeanPropertyRowMapper < Token > (Token.class));
    }
    
    public List<Token> findByGlobalToken(String globalToken) {
    	
        return jdbcTemplate.query("select * from token where Global_Token=?", new Object[] {
                globalToken
            },
            new BeanPropertyRowMapper < Token > (Token.class));
    }
    
    public int deleteById(String panToken) {
        return jdbcTemplate.update("delete from token where pan_Token=?", new Object[] {
            panToken
        });
    }
    public int insert(Token token) {
        return jdbcTemplate.update("insert into token (pan_Token, merchant_Token, global_Token) " + "values(?,  ?, ?)",
            new Object[] {
                token.getPanToken(), token.getMerchantToken(), token.getGlobalToken()
            });
    }
    public int update(Token token) {
        return jdbcTemplate.update("update token " + " set merchant_Token = ?, global_Token = ? " + " where pan_Token = ?",
            new Object[] {
                token.getMerchantToken(), token.getGlobalToken(), token.getPanToken()
            });
    }
}