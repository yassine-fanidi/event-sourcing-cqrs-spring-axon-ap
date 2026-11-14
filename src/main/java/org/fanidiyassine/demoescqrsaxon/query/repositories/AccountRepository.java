package org.fanidiyassine.demoescqrsaxon.query.repositories;

import org.fanidiyassine.demoescqrsaxon.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
