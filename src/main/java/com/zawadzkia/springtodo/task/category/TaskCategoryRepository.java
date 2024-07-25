package com.zawadzkia.springtodo.task.category;

import com.zawadzkia.springtodo.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategoryModel, Long>{
    List<TaskCategoryModel> findAllByOwner(UserModel owner);

    List<TaskCategoryModel> findAll();
}
