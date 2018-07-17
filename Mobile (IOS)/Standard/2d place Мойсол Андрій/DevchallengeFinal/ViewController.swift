//
//  ViewController.swift
//  DevchallengeFinal
//
//  Created by Someone on 6/23/18.
//  Copyright © 2018 Someone. All rights reserved.
//

import UIKit
import Vision

class ViewController: UIViewController {
    
    @IBOutlet weak var selectEffectButton: UIButton!
    @IBOutlet weak var saveImageButton: UIBarButtonItem!
    @IBOutlet weak var imageView: UIImageView!
    
    var image: UIImage? {
        didSet {
            imageView.image = image
        }
    }
    
    var faces: [VNFaceObservation]?
    
    var drawImage: UIImage!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }

    @IBAction func pickPhotoPressed(_ sender: Any) {
        CameraHandler.shared.imagePickedBlock = {(image: UIImage) in
            self.image = image
            self.saveImageButton.isEnabled = true
            self.selectEffectButton.isEnabled = true
            self.startFaceDetection()
        }
        CameraHandler.shared.showActionSheet(viewController: self)
    }
    
    func startFaceDetection() {
        faceDetection()
        if let faces = faces {
            guard !faces.isEmpty else {
                let errorAlert = UIAlertController(title: "Warning", message: "No faces on the photo", preferredStyle: .alert)
                errorAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                present(errorAlert, animated: true, completion: nil)
                return
            }
            let face = faces.first!
            addFaceRectToImage(face)
        } else {
            let errorAlert = UIAlertController(title: "Warning", message: "No faces on the photo", preferredStyle: .alert)
            errorAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            present(errorAlert, animated: true, completion: nil)
        }
    }
    
    func faceDetection() {
        if let image = image {
            var orientation: CGImagePropertyOrientation!
            switch image.imageOrientation {
            case .up:
                orientation = .up
            case .right:
                orientation = .right
            case .down:
                orientation = .down
            case .left:
                orientation = .left
            default:
                orientation = .up
            }
            let faceLandmarksRequest = VNDetectFaceLandmarksRequest(completionHandler: handleFaceFeatures)
            let requestHandler = VNImageRequestHandler(cgImage: image.cgImage!, orientation: orientation, options: [:])
            do {
                try requestHandler.perform([faceLandmarksRequest])
            } catch {
                print(error)
            }
        }
    }
    
    func handleFaceFeatures(request: VNRequest, errror: Error?) {
        guard let observations = request.results as? [VNFaceObservation] else {
            fatalError("Error")
        }
        
        faces = observations
    }
    
    func addFaceRectToImage(_ face: VNFaceObservation) {
        if let _ = image {
            let image = drawFaceRectangle(source: self.image!, boundingRect: face.boundingBox)
            self.imageView.image = image
        }
    }
    
    func addGlassesToImage(_ faces: [VNFaceObservation]) {
        self.imageView.image = self.image
        for face in faces {
            let leftEye = face.landmarks?.leftEye
            let rightEye = face.landmarks?.rightEye
            let leftEyeBrow = face.landmarks?.leftEyebrow
            
            let image = drawGlassesOnImage(source: self.imageView.image!, boundingRect: face.boundingBox, leftEye: leftEye!, rightEye: rightEye!, leftEyeBrow: leftEyeBrow!, withImage: drawImage)
            self.imageView.image = image
        }
    }
    
    func addLipToImage(_ faces: [VNFaceObservation]) {
        self.imageView.image = self.image
        for face in faces {
            let lip = face.landmarks?.outerLips
            
            let image = drawLipsOnImage(source: self.imageView.image!, boundingRect: face.boundingBox, faceLandmarkRegions: [lip!], withImage: drawImage)
            self.imageView.image = image
        }
    }
    
    func addMustacheToImage(_ faces: [VNFaceObservation]) {
        self.imageView.image = self.image
        for face in faces {
            let lips = face.landmarks?.outerLips
            let nose = face.landmarks?.nose
            
            let image = drawMustacheOnImage(source: self.imageView.image!, boundingRect: face.boundingBox, lips: lips!, nose: nose!, withImage: drawImage)
            self.imageView.image = image
        }
    }
    
    @IBAction func selectEffectPressed(_ sender: Any) {
        guard faces != nil else {
            let errorAlert = UIAlertController(title: "Warning", message: "Select a photo first", preferredStyle: .alert)
            errorAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            present(errorAlert, animated: true, completion: nil)
            return
        }
        let alertController = UIAlertController(title: nil, message: nil, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "Mustache #1", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "mustache1")
            self.addMustacheToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Mustache #2", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "mustache2")
            self.addMustacheToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Mustache #3", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "mustache3")
            self.addMustacheToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Glasses #1", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "glasses1")
            self.addGlassesToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Glasses #2", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "glasses2")
            self.addGlassesToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Glasses #3", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "glasses3")
            self.addGlassesToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Lips #1", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "lips1")
            self.addLipToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Lips #2", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "lips2")
            self.addLipToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Lips #3", style: .default, handler: { (action) in
            self.drawImage = #imageLiteral(resourceName: "lips3")
            self.addLipToImage(self.faces!)
        }))
        alertController.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        present(alertController, animated: true, completion: nil)
    }
    
    @IBAction func saveImagePressed(_ sender: Any) {
        guard self.image != nil else { return }
        UIGraphicsBeginImageContext(self.imageView.bounds.size)
        imageView.layer.render(in: UIGraphicsGetCurrentContext()!)
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        UIImageWriteToSavedPhotosAlbum(image!, self, #selector(image(_:didFinishSavingWithError:contextInfo:)), nil)
    }
    
    @objc func image(_ image: UIImage, didFinishSavingWithError error: Error?, contextInfo: UnsafeRawPointer) {
        if let error = error {
            let ac = UIAlertController(title: "Save error", message: error.localizedDescription, preferredStyle: .alert)
            ac.addAction(UIAlertAction(title: "OK", style: .default))
            present(ac, animated: true)
        } else {
            let ac = UIAlertController(title: "Saved!", message: "Your altered image has been saved to your photos.", preferredStyle: .alert)
            ac.addAction(UIAlertAction(title: "OK", style: .default))
            present(ac, animated: true)
        }
    }

}

