package org.fanidiyassine.demoescqrsaxon.query.repositories;

import org.fanidiyassine.demoescqrsaxon.query.entities.Account;
import org.fanidiyassine.demoescqrsaxon.query.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByAccountId(String accountId);
}
