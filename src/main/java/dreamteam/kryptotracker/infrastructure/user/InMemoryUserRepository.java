package dreamteam.kryptotracker.infrastructure.user;

import dreamteam.kryptotracker.domain.user.User;
import dreamteam.kryptotracker.domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dreamteam.kryptotracker.domain.user.UserState;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Mono<User> findByUsername(String username) {
        Optional<User> maybeUser = Optional.ofNullable(users.get(username));
        return Mono.justOrEmpty(maybeUser);
    }

    @Override
    public Mono<User> setUserState(User user, UserState userState) {
        Optional.ofNullable(users.get(user.getUsername()))
                .map(usr -> usr.withUserState(userState))
                .ifPresent(usr -> users.put(user.getUsername(), usr));
        return findByUsername(user.getUsername());
    }

    @Override
    public Flux<String> getAllUsernames() {
        return Flux.fromIterable(users.keySet());
    }

    @Override
    public Mono<User> add(User user) {
        users.put(user.getUsername(), user);
        return Mono.justOrEmpty(Optional.ofNullable(users.get(user.getUsername())));
    }

}
