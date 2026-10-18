#!/usr/bin/env python3
"""End-to-end smoke test of the Virtual HR stack through the gateway.

usage: smoke_test.py [base_url]   (default http://127.0.0.1:8088)
Exits non-zero if any check fails. Uses only the standard library.
"""
import json
import sys
import urllib.error
import urllib.request

BASE = (sys.argv[1] if len(sys.argv) > 1 else "http://127.0.0.1:8088").rstrip("/")
results = []


def call(method, path, body=None, token=None, headers=None):
    h = {"Content-Type": "application/json"}
    if token:
        h["Authorization"] = "Bearer " + token
    h.update(headers or {})
    data = json.dumps(body).encode() if body is not None else None
    req = urllib.request.Request(BASE + path, data=data, headers=h, method=method)
    try:
        with urllib.request.urlopen(req, timeout=120) as r:
            return r.status, r.headers, r.read()
    except urllib.error.HTTPError as e:
        return e.code, e.headers, e.read()


def check(name, ok, detail=""):
    results.append(ok)
    print(("PASS " if ok else "FAIL ") + name + (f"  ({detail})" if detail else ""))


s, h, b = call("GET", "/")
check("candidate UI /", s == 200 and b"<app-root" in b, s)
s, h, b = call("GET", "/manager/")
check("manager UI /manager/", s == 200 and b'id="root"' in b, s)
s, h, b = call("GET", "/manager/candidates")
check("manager SPA deep link", s == 200 and b'id="root"' in b, s)
s, h, b = call("GET", "/admin/login")
check("admin UI /admin/login", s == 200 and b"/admin/css/main.css" in b, s)


s, h, b = call("POST", "/api/auth/login", {"email": "eren@gmail.com", "password": "eren"})
token = json.loads(b).get("token") if s == 200 else None
check("login via jwt-provider (SOAP)", bool(token), s)

s, h, b = call("GET", "/api/geography/countries", token=token)
countries = json.loads(b) if s == 200 else []
check("cv-adapter -> cv-db seeded countries", s == 200 and len(countries) == 250, f"{s}, {len(countries)} countries")

s, h, b = call("GET", "/api/cvs", token=token)
check("JWT-protected /api/cvs", s == 200 and len(json.loads(b)) >= 1, s)
s, h, b = call("GET", "/api/cvs")
check("/api/cvs without token rejected", s == 401, s)

s, h, b = call("GET", "/api/manager/carts", token=token)
check("cv-adapter -> manager-db carts", s == 200, s)

s, h, b = call("GET", "/api/iq-tests", token=token)
check("cv-adapter iq-tests", s in (200, 204), s)

s, h, b = call("POST", "/api/cvs/filter", {"keywords": [], "minAge": 0, "maxAge": 100}, token=token)
check("cv-adapter -> cv-filter (Spark)", s == 200, f"{s} {b[:80]!r}")

q = "name=candidate_intro_1&category=candidate&subcategory=intro&resolution=720x720&language=en"
s, h, b = call("GET", "/avatar-api/avatar-info?" + q)
check("avatar-db scene info", s == 200 and b"textToSpeech" in b, s)
s, h, b = call("GET", "/avatar-api/avatar?" + q, headers={"Range": "bytes=0-1023"})
check("avatar video range request", s == 206 and h.get("Content-Type") == "video/mp4" and len(b) == 1024,
      f"{s} {h.get('Content-Range')}")

failed = results.count(False)
print(f"\n{len(results) - failed}/{len(results)} checks passed")
sys.exit(1 if failed else 0)
