package manager;

import data.Organization;
import data.Worker;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 5760575944040770153L;
    private String message = null;
    private Worker worker = null;
    private Organization organization= null;
    private String password = null;
    private String login = null;

    public Request(String message, Worker worker, String password, String login) {
        this.message = message;
        this.worker = worker;
        this.password = password;
        this.login = login;
    }
    public Request(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Organization getOrganization() {
        return organization;
    }
    public String getPassword() {
        return password;
    }
    public String getLogin() {
        return login;
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}