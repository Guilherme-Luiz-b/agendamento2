package br.chronos.Service;

import br.chronos.Entity.Local;
import br.chronos.Repository.LocalRepository;
import br.chronos.query.GenericSpesification;
import br.chronos.query.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalService {

        private final LocalRepository localRepository;

        public Page<Local> findAll(Request request) {
            return localRepository.findAll(new GenericSpesification<>(request.getList()), request.getPageable());
        }

        public Local findById(long id) {
            return localRepository.findById(id).orElse(null);
        }

        public Local save(Local local) throws Exception {
            return localRepository.save(local);
        }
}
