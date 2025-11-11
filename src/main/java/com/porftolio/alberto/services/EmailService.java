package com.porftolio.alberto.services;

import com.porftolio.alberto.models.ContactaConmigo;

public interface EmailService {
    void sendContactNotification(ContactaConmigo message);
}
