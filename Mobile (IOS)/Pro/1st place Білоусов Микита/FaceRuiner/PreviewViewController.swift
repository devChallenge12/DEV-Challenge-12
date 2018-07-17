import UIKit
import Vision

class PreviewViewController: UIViewController {
    
    // Property to set before presenting
    var image: UIImage!
    
    @IBOutlet weak var previewView: PreviewView!
    
    @IBAction func cancel(_ sender: Any) {
        dismiss(animated: true)
    }
    
    @IBAction func save(_ sender: Any) {
        // TODO:
        dismiss(animated: true)
    }
    
    @IBAction func debugModeValueChanged(_ _switch: UISwitch) {
        if _switch.isOn {
            previewView.showMask()
        } else {
            previewView.hideMask()
        }
    }
    
    private var face: VNFaceObservation? = nil
    private var selectedEffect = Effect(type: .glasses, image: #imageLiteral(resourceName: "glasses1"))
    
    override func viewDidLoad() {
        super.viewDidLoad()
        previewView.image = self.image
        processImage()
    }
    
    // performs face recognition and updates UI
    private func processImage() {
        
        var orientation: Int32 = 0
        
        // detect image orientation, we need it to be accurate for the face detection to work
        switch image.imageOrientation {
        case .up:
            orientation = 1
        case .right:
            orientation = 6
        case .down:
            orientation = 3
        case .left:
            orientation = 8
        default:
            orientation = 1
        }

        let faceDetectionRequest = VNDetectFaceLandmarksRequest(completionHandler: handleFaceLandmarks)
        let imageRequestHandler = VNImageRequestHandler(cgImage: image.cgImage!, orientation: CGImagePropertyOrientation(rawValue: UInt32(orientation))!, options: [:])
            
        // perform recognition on a background queue since it's an expensive
        // operation
        DispatchQueue.global().async {
            try? imageRequestHandler.perform([faceDetectionRequest])
        }
        
    }
    
    func handleFaceLandmarks(request: VNRequest, error: Error?) {
        
        guard let results = request.results as? [VNFaceObservation],
            let face = results.first else {
            return
        }
        
        self.face = face
        
        // perform UI updates on the main thread
        DispatchQueue.main.async {
            self.previewView.drawFaceWithLandmarks(face: face)
            self.previewView.hideMask()
            self.previewView.applyEffect(self.selectedEffect, to: face)
        }
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        if let effectListViewController = segue.destination as? EffectListViewController {
            effectListViewController.delegate = self
        }
    }
    
}

extension PreviewViewController: EffectListViewControllerDelegate {
    
    func didChooseEffect(_ effect: Effect) {
        if let faceUnwrapped = face {
            selectedEffect = effect
            previewView.applyEffect(effect, to: faceUnwrapped)
        }
    }
    
}
