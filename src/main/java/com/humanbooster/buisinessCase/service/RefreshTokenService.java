package com.humanbooster.buisinessCase.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.RefreshToken;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.RefreshTokenRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Users.
 * Provides methods to save, retrieve, update, and delete Users.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Saves a new Refresh Token.
     * @param refreshToken the Refresh Token to save
     * @return the newly saved Refresh Token
     */
    @Transactional
    public RefreshToken saveRefreshToken(RefreshToken refreshToken){
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Retrieves all Refresh Tokens.
     * @return a list of all Refresh Tokens
     */
    @Transactional(readOnly = true)
    public List<RefreshToken> getAllRefreshTokens(){
        return refreshTokenRepository.findAll();
    }

    /**
     * Retrieves a Refresh Token by its ID.
     * @param id the ID of the Refresh Token to retrieve
     * @return  an Optional containing the Refresh Token if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<RefreshToken> getRefreshTokenById(Long id){
        return refreshTokenRepository.findById(id);
    }
    
    /**
     * Retrieves a User by its Token.
     * @param token the Token of the User to retrieve
     * @return  an Optional containing the User if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByToken(String token){
        return refreshTokenRepository.findUserByToken(token);
    }

    /**
     * Deletes a Refresh Token by its ID.
     * @param id the ID of the Refresh Token to delete
     * @return an Optional containing the deleted Refresh Token if found, or empty if not found
     */
    @Transactional
    public Optional<RefreshToken> deleteRefreshTokenById(Long id){
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findById(id);
        refreshTokenOpt.ifPresent(refreshTokenRepository::delete);
        return refreshTokenOpt;
    }

    /**
     * Updates an existing Refresh Token.
     * @param id the ID of the Refresh Token to update
     * @param newRefreshToken the new Refresh Token data to update
     * @return an Optional containing the updated Refresh Token if found, or empty if not found
     */
    @Transactional
    public Optional<RefreshToken> updateRefreshToken(Long id, RefreshToken newRefreshToken){
        return refreshTokenRepository.findById(id)
                .map(existingRefreshToken -> {
                    ModelUtil.copyFields(newRefreshToken, existingRefreshToken);
                    return refreshTokenRepository.save(existingRefreshToken);
                });
    }

    /**
     * Checks if a Refresh Token exists by its ID.
     * @param id the ID of the Refresh Token to check
     * @return true if the Refresh Token exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return refreshTokenRepository.existsById(id);
    }
}
