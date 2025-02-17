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

/*
 *   Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package org.opensearch.alerting.resthandler

import org.apache.logging.log4j.LogManager
import org.opensearch.alerting.AlertingPlugin
import org.opensearch.alerting.action.GetMonitorAction
import org.opensearch.alerting.action.GetMonitorRequest
import org.opensearch.alerting.util.context
import org.opensearch.client.node.NodeClient
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.BaseRestHandler.RestChannelConsumer
import org.opensearch.rest.RestHandler.ReplacedRoute
import org.opensearch.rest.RestHandler.Route
import org.opensearch.rest.RestRequest
import org.opensearch.rest.RestRequest.Method.GET
import org.opensearch.rest.RestRequest.Method.HEAD
import org.opensearch.rest.action.RestActions
import org.opensearch.rest.action.RestToXContentListener
import org.opensearch.search.fetch.subphase.FetchSourceContext

private val log = LogManager.getLogger(RestGetMonitorAction::class.java)

/**
 * This class consists of the REST handler to retrieve a monitor .
 */
class RestGetMonitorAction : BaseRestHandler() {

    override fun getName(): String {
        return "get_monitor_action"
    }

    override fun routes(): List<Route> {
        return listOf()
    }

    override fun replacedRoutes(): MutableList<ReplacedRoute> {
        return mutableListOf(
            // Get a specific monitor
            ReplacedRoute(
                GET,
                "${AlertingPlugin.MONITOR_BASE_URI}/{monitorID}",
                GET,
                "${AlertingPlugin.LEGACY_OPENDISTRO_MONITOR_BASE_URI}/{monitorID}"
            ),
            ReplacedRoute(
                HEAD,
                "${AlertingPlugin.MONITOR_BASE_URI}/{monitorID}",
                HEAD,
                "${AlertingPlugin.LEGACY_OPENDISTRO_MONITOR_BASE_URI}/{monitorID}"
            )
        )
    }

    override fun prepareRequest(request: RestRequest, client: NodeClient): RestChannelConsumer {
        log.debug("${request.method()} ${AlertingPlugin.MONITOR_BASE_URI}/{monitorID}")

        val monitorId = request.param("monitorID")
        if (monitorId == null || monitorId.isEmpty()) {
            throw IllegalArgumentException("missing id")
        }

        var srcContext = context(request)
        if (request.method() == HEAD) {
            srcContext = FetchSourceContext.DO_NOT_FETCH_SOURCE
        }
        val getMonitorRequest = GetMonitorRequest(monitorId, RestActions.parseVersion(request), request.method(), srcContext)
        return RestChannelConsumer {
            channel ->
            client.execute(GetMonitorAction.INSTANCE, getMonitorRequest, RestToXContentListener(channel))
        }
    }
}
