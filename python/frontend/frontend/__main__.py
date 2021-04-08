import os
from frontend import create_app
from werkzeug.serving import run_simple

import beeline
from beeline.middleware.werkzeug import HoneyWSGIMiddleware
import beeline.propagation.w3c as w3c

beeline.init(
    # Get this via https://ui.honeycomb.io/account after signing up for Honeycomb
    writekey=os.environ.get("HONEYCOMB_WRITE_KEY"),
    # The name of your app is a good choice to start with
    dataset=os.environ.get("HONEYCOMB_DATASET"),
    service_name='python-greeting-frontend',
    debug=True,
    http_trace_parser_hook=w3c.http_trace_parser_hook,
    http_trace_propagation_hook=w3c.http_trace_propagation_hook
)


app = HoneyWSGIMiddleware(create_app())
run_simple('127.0.0.1', 7000, app, use_debugger=True, use_reloader=True)