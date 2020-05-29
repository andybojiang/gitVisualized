package com.gitlet.gitVisual.service;
import com.gitlet.gitVisual.model.Commit;
import com.gitlet.gitVisual.model.Repo;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ServiceTests {
    @Test
    public void addUserTest() {
        GitService service = new GitService();
        service.addUser();
    }

    @Test
    public void deleteUserTest() {
        GitService service = new GitService();
        UUID id = UUID.fromString("c5cfd28b-8ca2-4cf1-8166-f4af4b791e34");
        service.removeUser(id);
    }
    @Test
    public void addFileTest() {
        GitService service = new GitService();
        UUID id = UUID.fromString("25b6ed02-dae0-4b87-9e73-a82f594fe306");
        service.addFile(id, "hello.txt", "Hello World");
    }

    @Test
    public void rmFileTest() {
        GitService service = new GitService();
        UUID id = UUID.fromString("25b6ed02-dae0-4b87-9e73-a82f594fe306");
        service.removeFile(id, "hello.txt");
    }

    @Test
    public void editFileTest() {
        GitService service = new GitService();
        UUID id = UUID.fromString("25b6ed02-dae0-4b87-9e73-a82f594fe306");
        service.editFile(id, "hello.txt", "bye world");
    }

    @Test
    public void initTest() {
        GitService service = new GitService();
        UUID id = UUID.fromString("25b6ed02-dae0-4b87-9e73-a82f594fe306");
        List<String> result = service.process(id, "init");
        System.out.println(result);
    }
    @Test
    public void addTest() {
        GitService service = new GitService();
        UUID id = UUID.fromString("25b6ed02-dae0-4b87-9e73-a82f594fe306");
        service.process(id, "init");
        List<String> result = service.process(id, "add", "hello.txt");

        System.out.println(result);
    }

    @Test
    public void commitTest() {
        GitService service = new GitService();
        UUID id = UUID.fromString("25b6ed02-dae0-4b87-9e73-a82f594fe306");
        service.process(id, "init");
        List<String> result = service.process(id, "ewqrg2ergwe");

        System.out.println(result);
    }
}
