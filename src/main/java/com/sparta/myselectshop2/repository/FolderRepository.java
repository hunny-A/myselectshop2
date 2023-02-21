package com.sparta.myselectshop2.repository;

import com.sparta.myselectshop2.entity.Folder;
import com.sparta.myselectshop2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByUser(User user);
    List<Folder> findAllByUserAndNameIn(User user, List<String> names);

}