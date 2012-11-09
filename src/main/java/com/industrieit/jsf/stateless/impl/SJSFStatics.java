/*
 * Originally written r. simic
 * (c) 2011 Industrie It Pty Ltd
 * http://www.industrieit.com/
 *
 * IndustrieIT Pty Ltd licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * The software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.industrieit.jsf.stateless.impl;

public class SJSFStatics {

	public final static String BUILD_MARKER_KEY = "SJSF_BUILT";
	public final static String SJSF_MARKER = "SJSF";
	public final static String POOLABLE = "SJSF_POOLABLE";
	public final static String STATELESS_VIEW_ID = "STATELESS_JSF";
	public final static String STATELESS = "stateless";
	public static final String DISCRIMINATOR_ATTRIB_NAME = "SJSF_DISCRIMINATOR";
	public static final String DISCRIMINATOR = "discriminator";
	public static final String INPOOL = "SJSF_INPOOL";
	//you cannot create a view on a postback - you must get it from the buffer
	public static int POST_BUFFER = 3;
	//if extra views are created anything under non_contract_buffer is not cleaned up
	//ensure NON_CONTRACT_BUFFER>POST_BUFFER
	public static int NON_CONTRACT_BUFFER = 5;
}
