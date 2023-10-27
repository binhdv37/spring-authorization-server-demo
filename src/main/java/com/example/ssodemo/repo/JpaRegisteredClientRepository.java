package com.example.ssodemo.repo;

import com.example.ssodemo.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//@Component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ClientRepository clientRepository;
    private final StcClientAuthenticationMethodRepository stcClientAuthenticationMethodRepository;
    private final StcAuthorizationGrantTypeRepository stcAuthorizationGrantTypeRepository;
    private final RedirectUriRepository redirectUriRepository;
    private final PostLogoutRedirectUriRepository postLogoutRedirectUriRepository;
    private final ScopeRepository scopeRepository;


    public JpaRegisteredClientRepository(
            ClientRepository clientRepository,
            StcClientAuthenticationMethodRepository stcClientAuthenticationMethodRepository,
            StcAuthorizationGrantTypeRepository stcAuthorizationGrantTypeRepository,
            RedirectUriRepository redirectUriRepository,
            PostLogoutRedirectUriRepository postLogoutRedirectUriRepository,
            ScopeRepository scopeRepository
    ) {
        Assert.notNull(clientRepository, "clientRepository cannot be null");
        this.clientRepository = clientRepository;
        this.stcClientAuthenticationMethodRepository = stcClientAuthenticationMethodRepository;
        this.stcAuthorizationGrantTypeRepository = stcAuthorizationGrantTypeRepository;
        this.redirectUriRepository = redirectUriRepository;
        this.postLogoutRedirectUriRepository = postLogoutRedirectUriRepository;
        this.scopeRepository = scopeRepository;

        ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);              // Custom authorization grant type
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);      // Custom client authentication method
    }

    @PostConstruct
    void init() {
        // FIXME: remove, move this function to a API
        RegisteredClient existed = this.findByClientId("public-client");
        if (existed == null) {
            RegisteredClient registeredClient = RegisteredClient
                    .withId(UUID.randomUUID().toString())
                    .clientId("public-client")
                    .clientName("web admin")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .scope("openid")
                    .scope("profile")
                    .redirectUri("http://localhost:4200")
                    .build();
            this.save(registeredClient);
        }
    }

    @Transactional
    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        Client savedClient = this.clientRepository.save(toEntity(registeredClient));

        // client authentication method
        Set<StcClientAuthenticationMethod> clientAuthenticationMethods = registeredClient.getClientAuthenticationMethods().stream().map(x -> {
            StcClientAuthenticationMethod stcClientAuthenticationMethod = new StcClientAuthenticationMethod();
            stcClientAuthenticationMethod.setId(UUID.randomUUID().toString());
            stcClientAuthenticationMethod.setValue(x.getValue());
            stcClientAuthenticationMethod.setClient(savedClient);
            return stcClientAuthenticationMethod;
        }).collect(Collectors.toSet());
        stcClientAuthenticationMethodRepository.saveAll(clientAuthenticationMethods);

        // authorization grant types
        Set<StcAuthorizationGrantType> stcAuthorizationGrantTypes = registeredClient.getAuthorizationGrantTypes().stream().map(x -> {
            StcAuthorizationGrantType stcAuthorizationGrantType = new StcAuthorizationGrantType();
            stcAuthorizationGrantType.setId(UUID.randomUUID().toString());
            stcAuthorizationGrantType.setValue(x.getValue());
            stcAuthorizationGrantType.setClient(savedClient);
            return stcAuthorizationGrantType;
        }).collect(Collectors.toSet());
        stcAuthorizationGrantTypeRepository.saveAll(stcAuthorizationGrantTypes);

        // redirect uris
        Set<RedirectUri> redirectUris = registeredClient.getRedirectUris().stream().map(x -> {
            RedirectUri redirectUri = new RedirectUri();
            redirectUri.setId(UUID.randomUUID().toString());
            redirectUri.setValue(x);
            redirectUri.setClient(savedClient);
            return redirectUri;
        }).collect(Collectors.toSet());
        redirectUriRepository.saveAll(redirectUris);

        // post logout redirect uris
        Set<PostLogoutRedirectUri> postLogoutRedirectUris = registeredClient.getPostLogoutRedirectUris().stream().map(x -> {
            PostLogoutRedirectUri postLogoutRedirectUri = new PostLogoutRedirectUri();
            postLogoutRedirectUri.setId(UUID.randomUUID().toString());
            postLogoutRedirectUri.setValue(x);
            postLogoutRedirectUri.setClient(savedClient);
            return postLogoutRedirectUri;
        }).collect(Collectors.toSet());
        postLogoutRedirectUriRepository.saveAll(postLogoutRedirectUris);

        // scopes
        Set<Scope> scopes = registeredClient.getScopes().stream().map(x -> {
            Scope scope = new Scope();
            scope.setId(UUID.randomUUID().toString());
            scope.setValue(x);
            scope.setClient(savedClient);
            return scope;
        }).collect(Collectors.toSet());
        scopeRepository.saveAll(scopes);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.clientRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return this.clientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    private RegisteredClient toObject(Client client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName());

        if (client.getClientAuthenticationMethods() != null) {
            Set<ClientAuthenticationMethod> clientAuthenticationMethods = client.getClientAuthenticationMethods().stream().map(x -> {
                String authenticationMethod = x.getValue();
                return resolveClientAuthenticationMethod(authenticationMethod);
            }).collect(Collectors.toSet());
            builder.clientAuthenticationMethods(authenticationMethods -> authenticationMethods.addAll(clientAuthenticationMethods));
        }
        if (client.getAuthorizationGrantTypes() != null) {
            Set<AuthorizationGrantType> authorizationGrantTypes = client.getAuthorizationGrantTypes().stream().map(x -> {
                String grantType = x.getValue();
                return resolveAuthorizationGrantType(grantType);
            }).collect(Collectors.toSet());
            builder.authorizationGrantTypes(grantTypes -> grantTypes.addAll(authorizationGrantTypes));
        }
        if (client.getRedirectUris() != null) {
            Set<String> redirectUris = client.getRedirectUris().stream().map(RedirectUri::getValue).collect(Collectors.toSet());
            builder.redirectUris(uris -> uris.addAll(redirectUris));
        }
        if (client.getPostLogoutRedirectUris() != null) {
            Set<String> postLogoutRedirectUris = client.getPostLogoutRedirectUris().stream().map(PostLogoutRedirectUri::getValue).collect(Collectors.toSet());
            builder.postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris));
        }
        if (client.getScopes() != null) {
            Set<String> scopes = client.getScopes().stream().map(Scope::getValue).collect(Collectors.toSet());
            builder.scopes(clientScopes -> clientScopes.addAll(scopes));
        }

        Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
        builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

        return builder.build();
    }

    private Client toEntity(RegisteredClient registeredClient) {
//        List<String> clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
//        registeredClient.getClientAuthenticationMethods().forEach(clientAuthenticationMethod ->
//                clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));
//
//        List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
//        registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
//                authorizationGrantTypes.add(authorizationGrantType.getValue()));

        Client entity = new Client();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
//        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
//        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
//        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
//        entity.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
//        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}


