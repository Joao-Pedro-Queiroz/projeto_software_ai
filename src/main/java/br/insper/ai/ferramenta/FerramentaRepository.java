package br.insper.ai.ferramenta;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FerramentaRepository extends MongoRepository<Ferramenta, String> {
}
