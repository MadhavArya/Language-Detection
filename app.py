from flask import Flask, request, jsonify
import joblib

# Load model and vectorizer
model = joblib.load("model/language_model.pkl")
vectorizer = joblib.load("model/tfidf_vectorizer.pkl")

app = Flask(__name__)


@app.route("/predict", methods=["POST"])
def predict():
    data = request.json  # Get input data
    text = data.get("text", "")

    if not text:
        return jsonify({"error": "No text provided"}), 400

    # Transform text using TF-IDF
    text_tfidf = vectorizer.transform([text])

    # Predict language
    prediction = model.predict(text_tfidf)[0]

    return jsonify({"language": prediction})


if __name__ != "__main__":
    gunicorn_app = app  # Required for Render
else:
    app.run(debug=True)
