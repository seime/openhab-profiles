/**
 * Copyright (c) 2023 Contributors to the Seime Openhab Addons project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package no.seime.openhab.transform.profiles.internal.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.concurrent.Executors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.thing.profiles.ProfileCallback;
import org.openhab.core.thing.profiles.ProfileContext;
import org.openhab.core.thing.profiles.ProfileType;
import org.openhab.core.thing.profiles.ProfileTypeUID;
import org.openhab.core.thing.profiles.i18n.ProfileTypeI18nLocalizationService;
import org.openhab.core.util.BundleResolver;

/**
 * Basic unit tests for {@link ProfilesFactory}.
 *
 * @author Arne Seime - Initial contribution
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@NonNullByDefault
public class ProfilesFactoryTest {

    private static final int NUMBER_OF_PROFILES = 1;

    private @Mock @NonNullByDefault({}) ProfileTypeI18nLocalizationService mockLocalizationService;
    private @Mock @NonNullByDefault({}) BundleResolver mockBundleResolver;
    private @Mock @NonNullByDefault({}) ProfileCallback mockCallback;
    private @Mock @NonNullByDefault({}) ProfileContext mockContext;

    private @NonNullByDefault({}) ProfilesFactory profileFactory;

    @BeforeEach
    public void setup() {
        profileFactory = new ProfilesFactory(mockLocalizationService, mockBundleResolver);

        when(mockContext.getConfiguration()).thenReturn(new Configuration());
        when(mockContext.getExecutorService()).thenReturn(Executors.newSingleThreadScheduledExecutor());
    }

    @Test
    public void profileTypesAndUidsShouldBeAvailable() {
        Collection<ProfileTypeUID> supportedProfileTypeUIDs = profileFactory.getSupportedProfileTypeUIDs();
        assertThat(supportedProfileTypeUIDs, hasSize(NUMBER_OF_PROFILES));

        Collection<ProfileType> supportedProfileTypes = profileFactory.getProfileTypes(null);
        assertThat(supportedProfileTypes, hasSize(NUMBER_OF_PROFILES));

        for (ProfileType profileType : supportedProfileTypes) {
            assertTrue(supportedProfileTypeUIDs.contains(profileType.getUID()));
        }
    }

    @Test
    public void testFactoryCreatesAvailableProfiles() {
        for (ProfileTypeUID profileTypeUID : profileFactory.getSupportedProfileTypeUIDs()) {
            assertNotNull(profileFactory.createProfile(profileTypeUID, mockCallback, mockContext));
        }
    }
}
