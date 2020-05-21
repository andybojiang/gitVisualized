package com.gitlet.gitVisual.dao;

import com.gitlet.gitVisual.model.Repo;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
//FIXME: page is still in progress
@Repository("gitDao")
public class GitDao {


    /**
     * Creates a new repo with the correct directories
     */
    public void configRepo() {
        //FIXME
    }

    /**
     * Getter function for in-memory repo.
     * @return Repo object.
     */
    public Repo getRepo() {
        return _repo;
    }


    /**In-memory repository.*/
    private Repo _repo;
}
