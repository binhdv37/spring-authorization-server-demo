package com.example.ssodemo.repo;

import com.example.ssodemo.constant.ScopeConst;
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

import java.util.*;
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

        // save default scope, authorization grant type, client authentication method
        if (this.stcClientAuthenticationMethodRepository.count() == 0) {
            List<ClientAuthenticationMethod> authenticationMethods = Arrays.asList(
                    ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                    ClientAuthenticationMethod.CLIENT_SECRET_POST,
                    ClientAuthenticationMethod.CLIENT_SECRET_JWT,
                    ClientAuthenticationMethod.PRIVATE_KEY_JWT,
                    ClientAuthenticationMethod.NONE
            );
            List<StcClientAuthenticationMethod> stcClientAuthenticationMethods = authenticationMethods.stream().map(x -> {
                return new StcClientAuthenticationMethod(UUID.randomUUID().toString(), x.getValue());
            }).collect(Collectors.toList());
            this.stcClientAuthenticationMethodRepository.saveAll(stcClientAuthenticationMethods);
        }

        if (this.stcAuthorizationGrantTypeRepository.count() == 0) {
            List<AuthorizationGrantType> authorizationGrantTypes = Arrays.asList(
                    AuthorizationGrantType.AUTHORIZATION_CODE,
                    AuthorizationGrantType.REFRESH_TOKEN,
                    AuthorizationGrantType.CLIENT_CREDENTIALS,
                    AuthorizationGrantType.PASSWORD,
                    AuthorizationGrantType.JWT_BEARER,
                    AuthorizationGrantType.DEVICE_CODE
            );
            List<StcAuthorizationGrantType> stcAuthorizationGrantTypes = authorizationGrantTypes.stream().map(x -> {
                return new StcAuthorizationGrantType(UUID.randomUUID().toString(), x.getValue());
            }).collect(Collectors.toList());
            this.stcAuthorizationGrantTypeRepository.saveAll(stcAuthorizationGrantTypes);
        }

        if (this.scopeRepository.count() == 0) {
            List<String> scopeValues = Arrays.asList(ScopeConst.openid, ScopeConst.profile);
            List<Scope> scopes = scopeValues.stream().map(x -> {
                return new Scope(UUID.randomUUID().toString(), x);
            }).collect(Collectors.toList());
            this.scopeRepository.saveAll(scopes);
        }

//        RegisteredClient existed = this.findByClientId("web-admin-client");
//        if (existed == null) {
//            RegisteredClient registeredClient = RegisteredClient
//                    .withId(UUID.randomUUID().toString())
//                    .clientId("web-admin-client")
//                    .clientName("web admin")
//                    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                    .scope(ScopeConst.openid)
//                    .scope(ScopeConst.profile)
//                    .redirectUri("http://localhost:4300/auth/sso/callback/authorize")
//                    .build();
//            this.save(registeredClient);
//        }

        RegisteredClient client2 = this.findByClientId("client-2");
        if (client2 == null) {
            RegisteredClient registeredClient = RegisteredClient
                    .withId(UUID.randomUUID().toString())
                    .clientId("client-2")
                    .clientName("client 2")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .scope(ScopeConst.openid)
                    .scope(ScopeConst.profile)
                    .redirectUri("http://localhost:4200/callback")
                    .build();
            this.save(registeredClient);
        }

    }

    @Transactional
    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        Client savedClient = this.clientRepository.save(toEntity(registeredClient));

        // redirect uris
        Set<RedirectUri> redirectUris = registeredClient.getRedirectUris().stream().map(x -> {
            RedirectUri redirectUri = new RedirectUri();
            redirectUri.setId(UUID.randomUUID().toString());
            redirectUri.setUri(x);
            redirectUri.setClient(savedClient);
            return redirectUri;
        }).collect(Collectors.toSet());
        redirectUriRepository.saveAll(redirectUris);

        // post logout redirect uris
        Set<PostLogoutRedirectUri> postLogoutRedirectUris = registeredClient.getPostLogoutRedirectUris().stream().map(x -> {
            PostLogoutRedirectUri postLogoutRedirectUri = new PostLogoutRedirectUri();
            postLogoutRedirectUri.setId(UUID.randomUUID().toString());
            postLogoutRedirectUri.setUri(x);
            postLogoutRedirectUri.setClient(savedClient);
            return postLogoutRedirectUri;
        }).collect(Collectors.toSet());
        postLogoutRedirectUriRepository.saveAll(postLogoutRedirectUris);
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
                String authenticationMethod = x.getMethod();
                return resolveClientAuthenticationMethod(authenticationMethod);
            }).collect(Collectors.toSet());
            builder.clientAuthenticationMethods(authenticationMethods -> authenticationMethods.addAll(clientAuthenticationMethods));
        }
        if (client.getAuthorizationGrantTypes() != null) {
            Set<AuthorizationGrantType> authorizationGrantTypes = client.getAuthorizationGrantTypes().stream().map(x -> {
                String grantType = x.getGrantType();
                return resolveAuthorizationGrantType(grantType);
            }).collect(Collectors.toSet());
            builder.authorizationGrantTypes(grantTypes -> grantTypes.addAll(authorizationGrantTypes));
        }
        if (client.getRedirectUris() != null) {
            Set<String> redirectUris = client.getRedirectUris().stream().map(RedirectUri::getUri).collect(Collectors.toSet());
            builder.redirectUris(uris -> uris.addAll(redirectUris));
        }
        if (client.getPostLogoutRedirectUris() != null) {
            Set<String> postLogoutRedirectUris = client.getPostLogoutRedirectUris().stream().map(PostLogoutRedirectUri::getUri).collect(Collectors.toSet());
            builder.postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris));
        }
        if (client.getScopes() != null) {
            Set<String> scopes = client.getScopes().stream().map(Scope::getScope).collect(Collectors.toSet());
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

        // authentication methods
        List<StcClientAuthenticationMethod> authenticationMethods = this.stcClientAuthenticationMethodRepository.findAllByMethodIn(
                registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toList()));
        entity.setClientAuthenticationMethods(new HashSet<>(authenticationMethods));

        // authorization grant types
        List<StcAuthorizationGrantType> authorizationGrantTypes = this.stcAuthorizationGrantTypeRepository.findAllByGrantTypeIn(registeredClient
                .getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toList()));
        entity.setAuthorizationGrantTypes(new HashSet<>(authorizationGrantTypes));

        // scopes
        List<Scope> scopes = this.scopeRepository.findAllByScopeIn(registeredClient.getScopes().stream().toList());
        entity.setScopes(new HashSet<>(scopes));

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


