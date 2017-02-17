package ru.mit.spbau.antonpp.messanger.network;

import lombok.Builder;
import lombok.Data;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
@Data
@Builder
public class SignedMessage {
    private final String sender;
    private final String text;
}
