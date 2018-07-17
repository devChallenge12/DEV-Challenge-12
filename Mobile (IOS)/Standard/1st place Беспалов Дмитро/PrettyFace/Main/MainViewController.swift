//
//  ViewController.swift
//  PrettyFace

//  Copyright © 2018 DevChallenge. All rights reserved.
//

import UIKit
import QuartzCore

class MainViewController: UIViewController {

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var addBarButtonItem: UIBarButtonItem!
    
    private var recognizedViews: [UIView] = []
    private var initialCenter = CGPoint()
    
    private var currentImageSize: CGSize {
        guard let image = imageView.image
            else {
                return CGSize.zero
        }
        
        let width = imageView.bounds.width
        let height = image.size.height * (width / image.size.width)
        
        return CGSize(width: width, height: height)
    }
    
    private var currentImageOrigin: CGPoint {
        let imageSize = currentImageSize
        let x = (imageView.bounds.width - imageSize.width) / 2
        let y = (imageView.bounds.height - imageSize.height) / 2
        
        return CGPoint(x: x, y: y)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        addBarButtonItem.isEnabled = imageView.image != .none
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        clearRects()
        addRects()
    }
    
    @IBAction func openImage() {
        
        clearRects()
        
        let alertController = UIAlertController(title: "", message: "Select image from", preferredStyle: .actionSheet)
        
        let libraryAction = UIAlertAction(title: "Library", style: .default) { (action) in
            let imagePickerController = UIImagePickerController()
            imagePickerController.delegate = self
            imagePickerController.sourceType = .photoLibrary
            
            if UIImagePickerController.isSourceTypeAvailable(imagePickerController.sourceType) {
                self.present(imagePickerController, animated: true, completion: .none)
            }
        }
        alertController.addAction(libraryAction)
        
        let cameraAction = UIAlertAction(title: "Camera", style: .default) { (action) in
            let imagePickerController = UIImagePickerController()
            imagePickerController.delegate = self
            imagePickerController.sourceType = .camera
            imagePickerController.cameraCaptureMode = .photo
            if UIImagePickerController.isSourceTypeAvailable(imagePickerController.sourceType) {
                self.present(imagePickerController, animated: true, completion: .none)
            }
        }
        alertController.addAction(cameraAction)
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: .none)
        alertController.addAction(cancelAction)
        
        self.present(alertController, animated: true, completion: .none)
    }
    
    @IBAction func addEffects() {
        guard let collectionController = UIStoryboard(name: "Main", bundle: .main).instantiateViewController(withIdentifier: "EffectCollectionViewController") as? EffectsCollectionViewController
            else {
                return
        }
        
        collectionController.delegate = self
        let navigationController = UINavigationController(rootViewController: collectionController)
        self.present(navigationController, animated: true, completion: .none)
    }
    
    //MARK: Debug views
    
    func clearRects() {
        for view in recognizedViews {
            view.removeFromSuperview()
        }
        recognizedViews.removeAll()
        
        for view in imageView.subviews {
            view.removeFromSuperview()
        }
    }
    
    private func addRects() {
        
        guard let rects = FaceRecognitionManager.instance.detectedRects(imageOrigin: currentImageOrigin, imageSize: currentImageSize)
            else {
                return
        }
        
        for rect in rects {
            let customView = UIView(frame: rect)
            customView.layer.borderColor = #colorLiteral(red: 0.9757114053, green: 0.7531325221, blue: 0.2037431896, alpha: 1)
            customView.layer.borderWidth = 1.0
            imageView.addSubview(customView)
            recognizedViews.append(customView)
        }
        
        let customView = UIView(frame: CGRect(x: currentImageOrigin.x, y: currentImageOrigin.y, width: currentImageSize.width, height: currentImageSize.height))
        customView.layer.borderColor = #colorLiteral(red: 1, green: 0, blue: 0, alpha: 1)
        customView.layer.borderWidth = 1.0
        imageView.addSubview(customView)
    }
    
    //MARK: Gestures
    @IBAction func handlePinch(_ gestureRecognizer: UIPinchGestureRecognizer) {
        guard let view = gestureRecognizer.view
        else {
            return
        }
        
        //from Apple docs
        if gestureRecognizer.state == .began || gestureRecognizer.state == .changed {
            view.transform = view.transform.scaledBy(x: gestureRecognizer.scale, y: gestureRecognizer.scale)
            gestureRecognizer.scale = 1.0
        }
    }
    
    @IBAction func handlePan(_ gestureRecognizer: UIPanGestureRecognizer) {
        guard let view = gestureRecognizer.view
            else {
                return
        }
        
        let translation = gestureRecognizer.translation(in: view.superview)
        if gestureRecognizer.state == .began {
            initialCenter = view.center
        }
        if gestureRecognizer.state != .cancelled {
            let newCenter = CGPoint(x: initialCenter.x + translation.x, y: initialCenter.y + translation.y)
            view.center = newCenter
        }
        else {
            view.center = initialCenter
        }
    }
}

extension MainViewController: SelectedEffectDelegate {
    func didFinishPickingEffects(_ images: [UIImage]) {
        guard let rects = FaceRecognitionManager.instance.detectedRects(imageOrigin: currentImageOrigin, imageSize: currentImageSize)
            else {
                return
        }
        
        for rect in rects {
            let views = imageViews(images)
            for view in views {
                view.frame = rect
                let pinchGestureRecognizer = UIPinchGestureRecognizer(target: self, action: #selector(handlePinch(_:)))
                pinchGestureRecognizer.delegate = self
                view.addGestureRecognizer(pinchGestureRecognizer)
                let panGestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(handlePan(_:)))
                panGestureRecognizer.delegate = self
                view.addGestureRecognizer(panGestureRecognizer)
                view.isUserInteractionEnabled = true
                imageView.addSubview(view)
            }
        }
    }
    
    private func imageViews(_ images: [UIImage]) -> [UIImageView] {
        var views = [UIImageView]()
        for image in images {
            let effectImageView = UIImageView(image: image)
            effectImageView.contentMode = .scaleAspectFit
            views.append(effectImageView)
        }
        
        return views
    }
}

extension MainViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        self.dismiss(animated: true) {
            guard let image = info["UIImagePickerControllerOriginalImage"] as? UIImage
                else {
                    return
            }
            
            self.imageView.image = image
            self.addBarButtonItem.isEnabled = true
            FaceRecognitionManager.instance.recognize(image, orientation: image.imageOrientation, completion: {
                DispatchQueue.main.async {
                    self.addRects()
                }
            })
        }
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        self.dismiss(animated: true, completion: .none)
    }
}

extension MainViewController: UIGestureRecognizerDelegate {
    func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
}
