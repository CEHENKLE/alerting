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
 *   Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package org.opensearch.alerting.core

import org.joda.time.DateTime
import org.junit.Test
import org.opensearch.alerting.core.schedule.JobSchedulerMetrics
import org.opensearch.common.io.stream.BytesStreamOutput
import org.opensearch.common.io.stream.StreamInput
import org.opensearch.test.OpenSearchTestCase.assertEquals

class WriteableTests {

    @Test
    fun `test jobschedule metrics as stream`() {
        val metrics = JobSchedulerMetrics("test", DateTime.now().millis, false)
        val out = BytesStreamOutput()
        metrics.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newMetrics = JobSchedulerMetrics(sin)
        assertEquals("Round tripping metrics doesn't work", metrics.scheduledJobId, newMetrics.scheduledJobId)
    }
}
