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
import org.opensearch.action.support.WriteRequest
import org.opensearch.alerting.AlertingPlugin
import org.opensearch.alerting.action.IndexDestinationAction
import org.opensearch.alerting.action.IndexDestinationRequest
import org.opensearch.alerting.action.IndexDestinationResponse
import org.opensearch.alerting.model.destination.Destination
import org.opensearch.alerting.util.IF_PRIMARY_TERM
import org.opensearch.alerting.util.IF_SEQ_NO
import org.opensearch.alerting.util.REFRESH
import org.opensearch.client.node.NodeClient
import org.opensearch.common.xcontent.ToXContent
import org.opensearch.common.xcontent.XContentParser
import org.opensearch.common.xcontent.XContentParserUtils
import org.opensearch.index.seqno.SequenceNumbers
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.BaseRestHandler.RestChannelConsumer
import org.opensearch.rest.BytesRestResponse
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler.ReplacedRoute
import org.opensearch.rest.RestHandler.Route
import org.opensearch.rest.RestRequest
import org.opensearch.rest.RestResponse
import org.opensearch.rest.RestStatus
import org.opensearch.rest.action.RestResponseListener
import java.io.IOException

private val log = LogManager.getLogger(RestIndexDestinationAction::class.java)

/**
 * Rest handlers to create and update Destination
 */
class RestIndexDestinationAction : BaseRestHandler() {

    override fun getName(): String {
        return "index_destination_action"
    }

    override fun routes(): List<Route> {
        return listOf()
    }

    override fun replacedRoutes(): MutableList<ReplacedRoute> {
        return mutableListOf(
            ReplacedRoute(
                RestRequest.Method.POST,
                AlertingPlugin.DESTINATION_BASE_URI,
                RestRequest.Method.POST,
                AlertingPlugin.LEGACY_OPENDISTRO_DESTINATION_BASE_URI
            ),
            ReplacedRoute(
                RestRequest.Method.PUT,
                "${AlertingPlugin.DESTINATION_BASE_URI}/{destinationID}",
                RestRequest.Method.PUT,
                "${AlertingPlugin.LEGACY_OPENDISTRO_DESTINATION_BASE_URI}/{destinationID}"
            )
        )
    }

    @Throws(IOException::class)
    override fun prepareRequest(request: RestRequest, client: NodeClient): BaseRestHandler.RestChannelConsumer {
        log.debug("${request.method()} ${AlertingPlugin.DESTINATION_BASE_URI}")

        val id = request.param("destinationID", Destination.NO_ID)
        if (request.method() == RestRequest.Method.PUT && Destination.NO_ID == id) {
            throw IllegalArgumentException("Missing destination ID")
        }

        // Validate request by parsing JSON to Destination
        val xcp = request.contentParser()
        XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_OBJECT, xcp.nextToken(), xcp)
        val destination = Destination.parse(xcp, id)
        val seqNo = request.paramAsLong(IF_SEQ_NO, SequenceNumbers.UNASSIGNED_SEQ_NO)
        val primaryTerm = request.paramAsLong(IF_PRIMARY_TERM, SequenceNumbers.UNASSIGNED_PRIMARY_TERM)
        val refreshPolicy = if (request.hasParam(REFRESH)) {
            WriteRequest.RefreshPolicy.parse(request.param(REFRESH))
        } else {
            WriteRequest.RefreshPolicy.IMMEDIATE
        }
        val indexDestinationRequest = IndexDestinationRequest(id, seqNo, primaryTerm, refreshPolicy, request.method(), destination)
        return RestChannelConsumer {
            channel ->
            client.execute(
                IndexDestinationAction.INSTANCE, indexDestinationRequest,
                indexDestinationResponse(channel, request.method())
            )
        }
    }

    private fun indexDestinationResponse(channel: RestChannel, restMethod: RestRequest.Method):
        RestResponseListener<IndexDestinationResponse> {
        return object : RestResponseListener<IndexDestinationResponse>(channel) {
            @Throws(Exception::class)
            override fun buildResponse(response: IndexDestinationResponse): RestResponse {
                var returnStatus = RestStatus.CREATED
                if (restMethod == RestRequest.Method.PUT)
                    returnStatus = RestStatus.OK

                val restResponse = BytesRestResponse(returnStatus, response.toXContent(channel.newBuilder(), ToXContent.EMPTY_PARAMS))
                if (returnStatus == RestStatus.CREATED) {
                    val location = "${AlertingPlugin.DESTINATION_BASE_URI}/${response.id}"
                    restResponse.addHeader("Location", location)
                }
                return restResponse
            }
        }
    }
}
