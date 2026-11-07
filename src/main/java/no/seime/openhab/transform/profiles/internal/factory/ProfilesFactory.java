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

import static no.seime.openhab.transform.profiles.internal.ProfilesConstants.SCOPE;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.i18n.LocalizedKey;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.profiles.Profile;
import org.openhab.core.thing.profiles.ProfileAdvisor;
import org.openhab.core.thing.profiles.ProfileCallback;
import org.openhab.core.thing.profiles.ProfileContext;
import org.openhab.core.thing.profiles.ProfileFactory;
import org.openhab.core.thing.profiles.ProfileType;
import org.openhab.core.thing.profiles.ProfileTypeBuilder;
import org.openhab.core.thing.profiles.ProfileTypeProvider;
import org.openhab.core.thing.profiles.ProfileTypeUID;
import org.openhab.core.thing.profiles.i18n.ProfileTypeI18nLocalizationService;
import org.openhab.core.thing.type.ChannelType;
import org.openhab.core.util.BundleResolver;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import no.seime.openhab.transform.profiles.internal.profiles.RedeliverCommandProfile;

/**
 * The {@link ProfilesFactory} is responsible for creating profiles.
 *
 * @author Arne Seime - Initial contribution
 */
@Component(service = { ProfileFactory.class, ProfileTypeProvider.class })
@NonNullByDefault
public class ProfilesFactory implements ProfileFactory, ProfileTypeProvider, ProfileAdvisor {

    public static final ProfileTypeUID REDELIVER_COMMAND_UID = new ProfileTypeUID(SCOPE, "redeliver-command");

    private static final ProfileType PROFILE_TYPE_REDELIVER_COMMAND = ProfileTypeBuilder
            .newState(REDELIVER_COMMAND_UID, "Redeliver Command").build();

    private static final Set<ProfileTypeUID> SUPPORTED_PROFILE_TYPE_UIDS = Set.of(REDELIVER_COMMAND_UID);
    private static final Set<ProfileType> SUPPORTED_PROFILE_TYPES = Set.of(PROFILE_TYPE_REDELIVER_COMMAND);

    private final Map<LocalizedKey, ProfileType> localizedProfileTypeCache = new ConcurrentHashMap<>();

    private final ProfileTypeI18nLocalizationService profileTypeI18nLocalizationService;
    private final Bundle bundle;

    @Activate
    public ProfilesFactory(final @Reference ProfileTypeI18nLocalizationService profileTypeI18nLocalizationService,
            final @Reference BundleResolver bundleResolver) {
        this.profileTypeI18nLocalizationService = profileTypeI18nLocalizationService;
        bundle = bundleResolver.resolveBundle(ProfilesFactory.class);
    }

    @Override
    public @Nullable Profile createProfile(ProfileTypeUID profileTypeUID, ProfileCallback callback,
            ProfileContext context) {
        if (REDELIVER_COMMAND_UID.equals(profileTypeUID)) {
            return new RedeliverCommandProfile(callback, context);
        }
        return null;
    }

    @Override
    public Collection<ProfileType> getProfileTypes(@Nullable Locale locale) {
        return SUPPORTED_PROFILE_TYPES.stream().map(p -> createLocalizedProfileType(p, locale))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<ProfileTypeUID> getSupportedProfileTypeUIDs() {
        return SUPPORTED_PROFILE_TYPE_UIDS;
    }

    @Override
    public @Nullable ProfileTypeUID getSuggestedProfileTypeUID(ChannelType channelType, @Nullable String itemType) {
        return null;
    }

    @Override
    public @Nullable ProfileTypeUID getSuggestedProfileTypeUID(Channel channel, @Nullable String itemType) {
        return null;
    }

    private ProfileType createLocalizedProfileType(ProfileType profileType, @Nullable Locale locale) {
        final LocalizedKey localizedKey = new LocalizedKey(profileType.getUID(),
                locale != null ? locale.toLanguageTag() : null);

        final ProfileType cachedLocalizedProfileType = localizedProfileTypeCache.get(localizedKey);
        if (cachedLocalizedProfileType != null) {
            return cachedLocalizedProfileType;
        }

        final ProfileType localizedProfileType = profileTypeI18nLocalizationService.createLocalizedProfileType(bundle,
                profileType, locale);
        if (localizedProfileType != null) {
            localizedProfileTypeCache.put(localizedKey, localizedProfileType);
            return localizedProfileType;
        } else {
            return profileType;
        }
    }
}
