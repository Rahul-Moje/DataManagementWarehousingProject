package log_management;

import java.time.LocalDateTime;

import login.User;
import queries.QueryType;

public class Log {

    User user;
    LocalDateTime timestamp;
    QueryType query_type;
    String query;
    String result;


    public Log(User user, LocalDateTime timestamp, QueryType query_type, String query, String result) {
        this.user = user;
        this.timestamp = timestamp;
        this.query_type = query_type;
        this.query = query;
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public QueryType getQuery_type() {
        return query_type;
    }

    public void setQuery_type(QueryType query_type) {
        this.query_type = query_type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
