/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 *
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.alerting.settings

import org.opensearch.common.settings.Setting
import org.opensearch.common.unit.TimeValue
import java.util.concurrent.TimeUnit

/**
 * Legacy Opendistro settings specific to [AlertingPlugin]. These settings include things like history index max age, request timeout, etc...
 */

class LegacyOpenDistroAlertingSettings {

    companion object {

        const val MONITOR_MAX_INPUTS = 1
        const val MONITOR_MAX_TRIGGERS = 10

        val ALERTING_MAX_MONITORS = Setting.intSetting(
            "opendistro.alerting.monitor.max_monitors",
            1000,
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val INPUT_TIMEOUT = Setting.positiveTimeSetting(
            "opendistro.alerting.input_timeout",
            TimeValue.timeValueSeconds(30),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val INDEX_TIMEOUT = Setting.positiveTimeSetting(
            "opendistro.alerting.index_timeout",
            TimeValue.timeValueSeconds(60),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val BULK_TIMEOUT = Setting.positiveTimeSetting(
            "opendistro.alerting.bulk_timeout",
            TimeValue.timeValueSeconds(120),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val ALERT_BACKOFF_MILLIS = Setting.positiveTimeSetting(
            "opendistro.alerting.alert_backoff_millis",
            TimeValue.timeValueMillis(50),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val ALERT_BACKOFF_COUNT = Setting.intSetting(
            "opendistro.alerting.alert_backoff_count",
            2,
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val MOVE_ALERTS_BACKOFF_MILLIS = Setting.positiveTimeSetting(
            "opendistro.alerting.move_alerts_backoff_millis",
            TimeValue.timeValueMillis(250),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val MOVE_ALERTS_BACKOFF_COUNT = Setting.intSetting(
            "opendistro.alerting.move_alerts_backoff_count",
            3,
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val ALERT_HISTORY_ENABLED = Setting.boolSetting(
            "opendistro.alerting.alert_history_enabled",
            true,
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val ALERT_HISTORY_ROLLOVER_PERIOD = Setting.positiveTimeSetting(
            "opendistro.alerting.alert_history_rollover_period",
            TimeValue.timeValueHours(12),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val ALERT_HISTORY_INDEX_MAX_AGE = Setting.positiveTimeSetting(
            "opendistro.alerting.alert_history_max_age",
            TimeValue(30, TimeUnit.DAYS),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val ALERT_HISTORY_MAX_DOCS = Setting.longSetting(
            "opendistro.alerting.alert_history_max_docs",
            1000L,
            0L,
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val ALERT_HISTORY_RETENTION_PERIOD = Setting.positiveTimeSetting(
            "opendistro.alerting.alert_history_retention_period",
            TimeValue(60, TimeUnit.DAYS),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val REQUEST_TIMEOUT = Setting.positiveTimeSetting(
            "opendistro.alerting.request_timeout",
            TimeValue.timeValueSeconds(10),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val MAX_ACTION_THROTTLE_VALUE = Setting.positiveTimeSetting(
            "opendistro.alerting.action_throttle_max_value",
            TimeValue.timeValueHours(24),
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )

        val FILTER_BY_BACKEND_ROLES = Setting.boolSetting(
            "opendistro.alerting.filter_by_backend_roles",
            false,
            Setting.Property.NodeScope, Setting.Property.Dynamic, Setting.Property.Deprecated
        )
    }
}
